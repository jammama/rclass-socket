package com.learnershi.rclasssocket.router

import com.learnershi.rclasssocket.entity.common.ServerResult
import com.learnershi.rclasssocket.service.ClassRoomService
import com.learnershi.rclasssocket.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router


@Configuration
class RouterConfig(
    private val classRoomService: ClassRoomService,
    private val userService: UserService
) {

    @Bean
    fun classRoomRouter(): RouterFunction<ServerResponse> {
        return router {
            accept(MediaType.APPLICATION_JSON).nest {

                path("/classRoom").nest {
                    POST("/{userSeq}/create")(classRoomService::createClassRoom)
                    GET("/{classRoomId}")(classRoomService::getClassRoom)
                    PATCH("/{classRoomId}")(classRoomService::updateClassRoom)
                }

                // endpoint 만 작성. 추후 auth 서비스로 변경 고려
                path("/user").nest {
                    GET("/{classRoomId}")(userService::getUserClassRoomList)
                    GET("/list/{classRoomId}") { ServerResult.success().build() }
                }
            }

            // 추후 cms 서비스로 변경 고려
            accept(MediaType.MULTIPART_FORM_DATA).nest {
                path("/file").nest {
                    POST("/upload/{teacherId}/{classRoomId}"){ ServerResult.success().build() }
                    POST("/uploadFile/{teacherId}/{classRoomId}"){ ServerResult.success().build() }
                }
            }
        }
    }

}