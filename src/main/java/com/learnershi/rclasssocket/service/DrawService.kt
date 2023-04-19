package com.learnershi.rclasssocket.service

import com.learnershi.rclasssocket.entity.common.ServerResult
import com.learnershi.rclasssocket.log.Log
import com.learnershi.rclasssocket.repository.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Service
class DrawService(
    private val canvasDrawRepository: CanvasDrawRepository,
) {
    private val log = Log.of(this.javaClass)


    /**
     * 저장된 draw path List전송
     *
     * @param classRoomId 클래스룸 아이디
     * @return Mono<ServerResponse> 결과
    </ServerResponse> */
    fun sendCanvasDrawPathList(request: ServerRequest): Mono<ServerResponse> {
        val classRoomId = request.pathVariable("classRoomId")
        return canvasDrawRepository.findAllByClassRoomId(classRoomId)
            .collectList().flatMap { r -> ServerResult.success().data(r).build() }
    }
}
