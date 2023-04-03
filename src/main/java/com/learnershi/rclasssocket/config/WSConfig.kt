package com.learnershi.rclasssocket.config

import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.repository.ClassRoomUserRepository
import com.learnershi.rclasssocket.repository.SessionUserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WSConfig(
    private val classRoomUserRepository: ClassRoomUserRepository,
    private val kafkaTemplate: KafkaTemplate<String, Envelop>,
    private val sessionUserRepository: SessionUserRepository
) : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic")
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/connect").setAllowedOriginPatterns("*").withSockJS().setHeartbeatTime(10000)
    }


    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(
            SocketInterceptor(
                kafkaTemplate = kafkaTemplate,
                classRoomUserRepository = classRoomUserRepository,
                sessionUserRepository = sessionUserRepository
            )
        )
    }

//    @Bean
//    fun subRouter(classRoomInfoHandler: ClassRoomInfoHandler) = router {
//        accept(MediaType.APPLICATION_JSON).nest {
//            (GET("/api/user/") or GET("/api/users/")).invoke(classRoomInfoHandler::createClassRoom)
//            POST("/api/users/", classRoomInfoHandler::createClassRoom)
//        }
//        GET("/ahh") { ServerResponse.ok().bodyValue("되겠냐....") }
//    }
}