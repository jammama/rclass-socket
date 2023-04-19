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
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class SocketService(
    private val classRoomRepository: ClassRoomRepository,
    private val classUserSessionsRepository: ClassUserSessionsRepository
) {
    private val log = Log.of(this.javaClass)
    /**
     *  classRoom - User 세션에 사용자를 저장하고 classRoom 정보를 리턴한다.
     *  해당 classRoom이 없을 경우, 세션이 이미 있는 경우 에러
     */
    fun connect(classRoomId: String, user: User, requester: RSocketRequester): Mono<Envelop?> {
        classUserSessionsRepository.findUserByClassRoomIdAndUserSeq(classRoomId, user.seq)?.let {
            classUserSessionsRepository.removeUser(classRoomId, user.seq)
            // TODO: 기존 세션에
            return Mono.error {RuntimeException()}
        }
        return classRoomRepository.findById(classRoomId)
            // TODO: Exception 별도 처리
            .switchIfEmpty { Mono.error { RuntimeException() } }
            .doOnSuccess {
                requester.rsocket()!!
                    .onClose()
                    .doFirst {
                        classUserSessionsRepository.addUserSession(classRoomId, UserSession(user, requester))
                        log.info("---user On {} {}", classRoomId, user.seq)
                    }.doFinally {
                        classUserSessionsRepository.removeUser(classRoomId, user.seq)
                        log.info("---user Off {} {}", classRoomId, user.seq)
                    }.subscribe()
            }.map {
                Envelop(
                    msgType = MessageType.CONNECT,
                    userType = UserType.ALL,
                    data = it,
                    classRoomId = classRoomId
                )
            }
    }
}