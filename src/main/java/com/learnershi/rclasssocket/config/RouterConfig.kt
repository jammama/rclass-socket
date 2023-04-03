package com.learnershi.rclasssocket.config

import com.learnershi.rclasssocket.service.ClassRoomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfig(
    private val classRoomService: ClassRoomService
) {
    @Bean
    fun classRoomRouter(): RouterFunction<ServerResponse> {
        return coRouter {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/user/{classRoomId}"){ classRoomService.getClassRoom(it.pathVariable("classRoomId")).block()!! }
            }
        }
    }
}