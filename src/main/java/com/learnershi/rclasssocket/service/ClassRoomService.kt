package com.learnershi.rclasssocket.service

import com.learnershi.rclasssocket.entity.ClassRoom
import com.learnershi.rclasssocket.entity.common.ServerResult
import com.learnershi.rclasssocket.entity.enums.ClassState
import com.learnershi.rclasssocket.log.Log
import com.learnershi.rclasssocket.repository.ClassRoomRepository
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class ClassRoomService(
    private val classRoomRepository: ClassRoomRepository,
) {
    private val log = Log.of(this.javaClass)

    /**
     * ClassRoom을 새로 생성한다.
     *
     * @param request ServerRequest
     * @return Mono<ServerResponse> 응답
    */
    fun createClassRoom(userSeq: String, userName: String): Mono<ServerResponse> {
        log.info("create class - {} {}", userSeq, userName)
        val newClassRoom = ClassRoom(
            teacherSeq = userSeq,
            teacherName = userName,
            roomState = ClassState.WAIT,
            startDate = LocalDateTime.now()
        )
        return classRoomRepository.save(newClassRoom).flatMap { r -> ServerResult.success().data(r).build() }
    }

    /**
     * ClassRoom를 조회한다
     *
     * @param request ServerRequest
     * @return Mono<ServerResponse> 응답
    */
    fun getClassRoom(classRoomId: String): Mono<ServerResponse> {
        log.info("get class - {}", classRoomId)
        return classRoomRepository.findById(classRoomId).flatMap { r -> ServerResult.success().data(r).build() }
    }
    /**
     * ClassRoom를 조회한다
     *
     * @param request ServerRequest
     * @return Mono<ServerResponse> 응답
     */
    fun getClassRoom(request: ServerRequest): Mono<ServerResponse> {
        val classRoomId = request.pathVariable("classRoomId")
        log.info("get class - {}", classRoomId)
        return classRoomRepository.findById(classRoomId).flatMap { r -> ServerResult.success().data(r).build() }
    }
}