package com.learnershi.rclasssocket.controller

import com.learnershi.rclasssocket.service.ClassRoomService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@RestController
class HttpController(
    private val classRoomService: ClassRoomService
) {
    /**
     * classRoomId로 classRoom을 조회한다.
     *
     * @param classRoomId 클래스룸Id
     * @return classRoom 생성된 클래스
     */
    @GetMapping("/classRoom/{classRoomId}")
    fun getClassRoom(
        @PathVariable classRoomId : String
    ): ResponseEntity<Mono<ServerResponse>> {
        return ResponseEntity.ok().body(classRoomService.getClassRoom(classRoomId))
    }

    /**
     * classRoom을 생성한다
     *
     * @param userSeq 사용자 seq
     * @param userName 사용자명
     * @return classRoom 생성된 클래스
     */
    @PostMapping("/classRoom/{userSeq}/create")
    fun createClassRoom(
        @PathVariable userSeq: String,
        @RequestParam userName: String
    ): Mono<ServerResponse> {
        return classRoomService.createClassRoom(userSeq, userName)
    }
}