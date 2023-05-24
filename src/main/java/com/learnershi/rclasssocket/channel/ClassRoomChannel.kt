package com.learnershi.rclasssocket.channel

import com.learnershi.rclasssocket.entity.ClassRoom
import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.entity.enums.UserType
import com.learnershi.rclasssocket.repository.ClassRoomRepository
import com.learnershi.rclasssocket.repository.ClassUserSessionsRepository
import com.learnershi.rclasssocket.repository.UserSession
import org.slf4j.LoggerFactory
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * ClassRoom 내 requester 별 전송 채널
 */
@Component
class ClassRoomChannel(
    private val classroomUsersSession: ClassUserSessionsRepository,
    private val classRoomRepository: ClassRoomRepository
) {
    private val log = LoggerFactory.getLogger(this.javaClass)


    /**
     * 연결된 사용자 중
     * ClassRoomId, 강사/학생 여부가 맞는 사용자를 분류해
     * envelop을 전송한다.
     *
     * @param envelop 메세지 객체
     */
    fun send(envelop: Envelop) {
        log.debug("ClassRoomChannel.send - msgType: {}", envelop.msgType)
        val classRoomId = envelop.classRoomId
        getClassRoomUsersByType(classRoomId, envelop.userType).subscribe { userSessions: Collection<UserSession?> ->
            userSessions.map { r: UserSession? -> r?.let { sendToRequester(it.requester, envelop) } }
        }
    }
    /**
     * userSeq에 해당하는 사용자에게 envelop을 전송한다.
     *
     * @param requester RSocketRequester
     * @param envelop 메세지 객체
     */
    fun sendToRequester(requester: RSocketRequester, envelop: Envelop) {
        log.debug("send to client {}", envelop.userType.toString())
        requester
            .route("")
            .data(envelop)
            .send()
            .subscribe()
    }

    /**
     * 저장 된 classRoom 내의 UserType별로 유저를 분류한다.
     *
     * @param classRoomId classRoomId
     * @param type UserType
     * @return 분류된 유저
     */
    private fun getClassRoomUsersByType(classRoomId: String, type: UserType?): Mono<Collection<UserSession?>> {
        return classRoomRepository.findById(classRoomId)
            .mapNotNull { classRoom: ClassRoom? ->
                val users = classroomUsersSession.findByClassRoomId(classRoomId)
                when (type) {
                    UserType.TEACHER -> users.filter { userSession: UserSession -> classRoom!!.teacherSeq.equals(userSession.user.seq) }
                    UserType.STUDENT -> users.filter { userSession: UserSession -> !classRoom!!.teacherSeq.equals(userSession.user.seq) }
                    else -> users
                }
            }
    }

}