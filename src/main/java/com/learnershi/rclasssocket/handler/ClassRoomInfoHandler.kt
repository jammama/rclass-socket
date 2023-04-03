package com.learnershi.rclasssocket.handler

import com.learnershi.rclasssocket.entity.ClassRoom
import com.learnershi.rclasssocket.entity.common.ServerResult
import com.learnershi.rclasssocket.entity.enums.ClassState
import com.learnershi.rclasssocket.repository.ClassRoomRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Component
class ClassRoomInfoHandler(
    private val classRoomRepository: ClassRoomRepository
) : DefaultHandler() {
    /**
     * ClassRoom을 새로 생성한다.
     *
     * @param request ServerRequest
     * @return Mono<ServerResponse> 응답
    */
    fun createClassRoom(request: ServerRequest): Mono<ServerResponse> {
        logInfo("createClassRoom")
        val userSeq = request.pathVariable("userSeq")
        val userName = request.queryParam("userName").orElse("강사")
        val newClassRoom = ClassRoom()
        newClassRoom.teacherSeq = userSeq
        newClassRoom.teacherName = userName
        newClassRoom.roomState = ClassState.WAIT
        newClassRoom.startDate = LocalDateTime.now()

        return classRoomRepository.save(newClassRoom).flatMap { r -> ServerResult.success().data(r).build() }
    }
}