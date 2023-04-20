package com.learnershi.rclasssocket.handler

import com.learnershi.rclasssocket.entity.ClassRoom
import com.learnershi.rclasssocket.entity.common.ServerResult
import com.learnershi.rclasssocket.log.Log
import com.learnershi.rclasssocket.service.ClassRoomService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

/**
 * Handler의 공통 로직을 담당하는 클래스
 */
@Component
class DefaultHandler(
    val classRoomService: ClassRoomService
) {
    private val log = Log.of(this.javaClass)
    fun logInfo(logFormat: String?, vararg objects: Any?) {
        log.info(MARKER_HANDLER, logFormat, objects)
    }

    companion object {
        private const val MARKER_HANDLER = "[HANDLER]"
    }
    /**
     * ClassRoom를 생성한다
     *
     * @param request 요청
     * @return Mono<ServerResponse> 응답
     */
    fun createClassRoom(request: ServerRequest): Mono<ServerResponse> {
        val userSeq = request.pathVariable("userSeq")
        val userName = request.queryParam("userName").orElse("익명의 강사")
        log.info("create class - {} {}", userSeq, userName)
        return classRoomService.createClassRoom(userSeq, userName).flatMap { ServerResult.success().data(it).build() }
    }
    /**
     * ClassRoom를 조회한다
     *
     * @param request 요청
     * @return Mono<ServerResponse> 응답
     */
    fun getClassRoom(request: ServerRequest): Mono<ServerResponse> {
        val classRoomId = request.pathVariable("classRoomId")
        log.info("get class - {}", classRoomId)
        return classRoomService.getClassRoom(classRoomId).flatMap { ServerResult.success().data(it).build() }
    }

    /**
     * ClassRoom를 수정한다
     *
     * @param request 요청
     * @return Mono<ServerResponse> 응답
     */
    fun updateClassRoom(request: ServerRequest): Mono<ServerResponse> {
        val classRoomId = request.pathVariable("classRoomId")
        val patchRoomMono = request.bodyToMono<ClassRoom>()
        return patchRoomMono.flatMap { classRoomService.updateClassRoom(classRoomId, it)
            .flatMap { r -> ServerResult.success().data(r).build() }
        }
    }
}