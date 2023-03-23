package com.learnershi.rclasssocket.config;

import com.learnershi.rclasssocket.entity.common.Envelop;
import com.learnershi.rclasssocket.repository.ClassRoomUserRepository;
import com.learnershi.rclasssocket.repository.SessionUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WSConfig implements WebSocketMessageBrokerConfigurer {
    private final ClassRoomUserRepository classRoomUserRepository;
    private final KafkaTemplate<String, Envelop> kafkaTemplate;
    private final SessionUserRepository sessionUserRepository;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setHeartbeatTime(10000);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(SocketInterceptor.builder()
                .kafkaTemplate(kafkaTemplate)
                .classRoomUserRepository(classRoomUserRepository)
                .sessionUserRepository(sessionUserRepository)
                .build());
    }

}
