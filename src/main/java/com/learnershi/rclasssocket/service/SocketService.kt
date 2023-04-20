package com.learnershi.rclasssocket.service

import com.learnershi.rclasssocket.entity.User
import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.entity.enums.MessageType
import com.learnershi.rclasssocket.entity.enums.UserType
import com.learnershi.rclasssocket.log.Log
import com.learnershi.rclasssocket.repository.ClassRoomRepository
import com.learnershi.rclasssocket.repository.ClassUserSessionsRepository
import com.learnershi.rclasssocket.repository.UserSession
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SocketService(
    private val classRoomRepository: ClassRoomRepository,
    private val classUserSessionsRepository: ClassUserSessionsRepository
) {
    private val log = Log.of(this.javaClass)

    /**
     *  classRoom - User 세션에 사용자를 저장하고 user 정보가 담긴 Envelop을 리턴한다.
     *  세션이 이미 있는 경우 에러
     *  @param classRoomId - classRoomId
     *  @param user - User
     *  @param requester - RSocketRequester
     *  @return Mono<Envelop> - CONNECT, ALL : {connect 된 user 정보}
     */
    fun connect(classRoomId: String, user: User, requester: RSocketRequester): Mono<Envelop> {
        // session error 처리
        classUserSessionsRepository.findUserByClassRoomIdAndUserSeq(classRoomId, user.seq)?.let {
            classUserSessionsRepository.removeUser(classRoomId, user.seq)
            throw RuntimeException("이미 접속중인 사용자입니다.")
        }
        return requester.rsocket()!!
            .onClose()
            .doFirst {
                classUserSessionsRepository.addUserSession(classRoomId, UserSession(user, requester))
                log.info("---user On {} {}", classRoomId, user.seq)
            }.doFinally {
                classUserSessionsRepository.removeUser(classRoomId, user.seq)
                log.info("---user Off {} {}", classRoomId, user.seq)
            }.thenReturn(
                Envelop(
                    msgType = MessageType.CONNECT,
                    userType = UserType.ALL,
                    data = user, // classRoom
                    classRoomId = classRoomId
                )
            )
    }

}