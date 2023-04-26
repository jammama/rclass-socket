package com.learnershi.rclasssocket.service

import com.learnershi.rclasssocket.controller.EnvelopSendService
import com.learnershi.rclasssocket.entity.User
import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.entity.enums.MessageType
import com.learnershi.rclasssocket.entity.enums.UserType
import com.learnershi.rclasssocket.log.Log
import com.learnershi.rclasssocket.repository.ClassUserSessionsRepository
import com.learnershi.rclasssocket.repository.UserSession
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Service

@Service
class SocketService(
    private val envelopSendService: EnvelopSendService,
    private val classUserSessionsRepository: ClassUserSessionsRepository
) {
    private val log = Log.of(this.javaClass)

    /**
     *  유저 접속 처리
     *  @param classRoomId - classRoomId
     *  @param user - User
     *  @param requester - RSocketRequester
     */
    fun connect(classRoomId: String, user: User, requester: RSocketRequester) {
        // session error 처리
        classUserSessionsRepository.findUserByClassRoomIdAndUserSeq(classRoomId, user.seq)?.let {
            classUserSessionsRepository.removeUser(classRoomId, user.seq)
            throw RuntimeException("이미 접속중인 사용자입니다.")
        }
        requester.rsocket()!!
            .onClose()
            .doFirst {
                connectUser(classRoomId, user, requester)
            }.doFinally {
                disconnectUser(classRoomId, user)
            }.subscribe()
    }

    /**
     * 유저를 세션에서 제거하고 메세지 전송
     */
    private fun disconnectUser(classRoomId: String, user: User) {
        classUserSessionsRepository.removeUser(classRoomId, user.seq)
        log.info("---user Off {} {}", classRoomId, user.seq)
        envelopSendService.sendMessageQueue(
            Envelop(
                msgType = MessageType.DISCONNECT,
                userType = UserType.ALL,
                data = user, // classRoom
                classRoomId = classRoomId
            )
        )
    }

    /**
     * 유저의 세션(requester)을 저장하고 메세지 전송
     */
    private fun connectUser(
        classRoomId: String,
        user: User,
        requester: RSocketRequester
    ) {
        classUserSessionsRepository.addUserSession(classRoomId, UserSession(user, requester))
        log.info("---user On {} {}", classRoomId, user.seq)
        envelopSendService.sendMessageQueue(
            Envelop(
                msgType = MessageType.CONNECT,
                userType = UserType.ALL,
                data = user, // classRoom
                classRoomId = classRoomId
            )
        )
    }

}