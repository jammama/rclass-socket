package com.learnershi.rclasssocket.service

import com.learnershi.rclasssocket.entity.common.ServerResult
import com.learnershi.rclasssocket.entity.enums.UserType
import com.learnershi.rclasssocket.log.Log
import com.learnershi.rclasssocket.repository.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Service
class UserService(
    private val classRoomRepository: ClassRoomRepository,
) {
    private val log = Log.of(this.javaClass)

    /**
     * ClassRoom를 조회한다
     *
     * @param classRoomId 클래스룸 아이디
     * @return Mono<ServerResponse> 응답
     */
    fun getUserClassRoomList(request: ServerRequest): Mono<ServerResponse> {
        val classRoomId = request.pathVariable("classRoomId")
        val userSeq = request.pathVariable("userSeq")
        val userType = UserType.valueOf(request.pathVariable("userType"))
        val page = request.queryParam("page")
        val size = request.queryParam("size")
        log.debug("get class - {}", classRoomId)
        // TODO : 헤헤 하기 싫어서 그냥 냅둠
        return classRoomRepository.findById(classRoomId).flatMap { r -> ServerResult.success().data(r).build() }
    }


}