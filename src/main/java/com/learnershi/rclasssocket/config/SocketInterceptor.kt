package com.learnershi.rclasssocket.config

import com.learnershi.rclasssocket.controller.MessageListener
import com.learnershi.rclasssocket.entity.User
import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.entity.enums.MessageType
import com.learnershi.rclasssocket.entity.enums.UserType
import com.learnershi.rclasssocket.repository.ClassRoomUserRepository
import com.learnershi.rclasssocket.repository.SessionUserRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor

class SocketInterceptor(
    private val classRoomUserRepository: ClassRoomUserRepository,
    private val sessionUserRepository: SessionUserRepository,
    private val kafkaTemplate: KafkaTemplate<String, Envelop>
) : ChannelInterceptor {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)!!
        if (StompCommand.CONNECT == accessor.command) {
            val sessionId = accessor.sessionId!!
            val classRoomId = accessor.getNativeHeader("classRoomId")?.get(0)!!
            val userSeq = accessor.getNativeHeader("userSeq")?.get(0)!!
            val userName = accessor.getNativeHeader("userName")?.get(0)!!
            connectUser(sessionId, classRoomId, userSeq, userName)
        } else if (StompCommand.DISCONNECT == accessor.command) {
            val sessionId = accessor.sessionId!!
            disconnectUser(sessionId)
        }
        return message
    }

    /**
     * 사용자 접속.
     *
     * @param sessionId 웹소켓 세션 Id
     * @param classRoomId 클래스룸 Id
     * @param userSeq 사용자 Id
     * @param userName 사용자 이름
     */
    private fun connectUser(sessionId: String, classRoomId: String, userSeq: String, userName: String) {
        sessionUserRepository.addSession(sessionId, userSeq, classRoomId)
        val newUser = User(userSeq, userName)
        classRoomUserRepository.addUser(classRoomId, newUser)
        kafkaTemplate.send(
            MessageListener.TOPIC,
            Envelop(
                msgType = MessageType.CONNECT,
                data = newUser,
                userType = UserType.ALL,
                classRoomId = classRoomId
            )
        )
    }

    /**
     * 사용자 접속 해제.
     *
     * @param sessionId 웹소켓 세션 Id
     */
    private fun disconnectUser(sessionId: String) {
        val classRoomId = sessionUserRepository.getClassRoomId(sessionId)
        val userSeq = sessionUserRepository.getUserSeq(sessionId)
        val removeUser = classRoomUserRepository.removeUser(classRoomId, userSeq)
        sessionUserRepository.removeSession(sessionId)
        kafkaTemplate.send(
            MessageListener.TOPIC,
            Envelop(
                msgType = MessageType.DISCONNECT,
                data = removeUser ?: User(userSeq, ""),
                userType = UserType.ALL,
                classRoomId = classRoomId
            )
        )
    }
}