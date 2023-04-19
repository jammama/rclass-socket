package com.learnershi.rclasssocket.router

import com.learnershi.rclasssocket.service.ClassRoomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class RouterConfig(
    private val classRoomService: ClassRoomService
) {
    @Bean
    fun classRoomRouter(): RouterFunction<ServerResponse> {
        return router {
            accept(MediaType.APPLICATION_JSON).nest {
                path("/classroom").nest {
                    POST("/{userSeq}") {
                        classRoomService.createClassRoom(
                            it.pathVariable("userSeq"),
                            it.pathVariable("userName"),
                        )
                    }
                }
                path("/user").nest {
                    GET("/{classRoomId}") {
                        classRoomService.getClassRoom(it.pathVariable("classRoomId"))
                    }
                }


            }
        }
    }

}