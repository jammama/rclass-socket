package com.learnershi.rclasssocket.channel

import com.learnershi.rclasssocket.Log.Log
import com.learnershi.rclasssocket.entity.ClassRoom
import com.learnershi.rclasssocket.entity.User
import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.entity.enums.UserType
import com.learnershi.rclasssocket.repository.ClassRoomRepository
import com.learnershi.rclasssocket.repository.ClassRoomUserRepository
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.function.Consumer

/**
 * ClassRoom 전송 채널
 */
@Component
class ClassRoomChannel(
    private val classroomUserRepository: ClassRoomUserRepository,
    private val classRoomRepository: ClassRoomRepository,
    private val simpMessagingTemplate: SimpMessagingTemplate
) {


    // 소켓 publish 주소
    private val DESTINATION = "/msg"

    private val log = Log.logger(this)

    /**
     * userSeq에 해당하는 사용자에게 envelop을 전송한다.
     *
     * @param userSeq 사용자 seq
     * @param envelop 메세지 객체
     */
    fun sendToUser(userSeq: String, envelop: Envelop) {
        simpMessagingTemplate.convertAndSendToUser(userSeq, DESTINATION, envelop)
    }

    /**
     * 현재 연결되어있는 사용자 중
     * envelop 내의 ClassRoomId로 classRoom을 조회하고
     * classRoom 내 강사/학생 여부를 분류해
     * 소켓을 통해 envelop을 전송한다.
     *
     * @param envelop 메세지 객체
     */
    fun send(envelop: Envelop) {
        log.info("ClassRoomChannel.send - msgType: {}", envelop.msgType)
        val classRoomId = envelop.classRoomId
        getClassRoomUsersByType(classRoomId, envelop.userType).subscribe { users: Collection<User?> ->
            users.forEach(
                Consumer { user: User? -> user?.let { sendToUser(it.seq, envelop) } })
        }
    }

    /**
     * 저장 된 classRoom 내의 UserType별로 유저를 분류한다.
     *
     * @param classRoomId classRoomId
     * @param type UserType
     * @return 분류된 유저
     */
    private fun getClassRoomUsersByType(classRoomId: String, type: UserType?): Mono<Collection<User?>> {
        return classRoomRepository.findById(classRoomId)
            .map { classRoom: ClassRoom? ->
                val users = classroomUserRepository.findByClassRoomId(classRoomId).filterNotNull()
                when (type) {
                    UserType.T -> users.filter { user: User -> classRoom!!.isTeacher(user.seq) }
                    UserType.S -> users.filter { user: User -> !classRoom!!.isTeacher(user.seq) }
                    else -> users
                }
            }
    }
}