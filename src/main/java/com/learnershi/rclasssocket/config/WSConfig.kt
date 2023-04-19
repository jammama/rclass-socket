package com.learnershi.rclasssocket.config

import com.learnershi.rclasssocket.log.Log
import org.springframework.context.annotation.Configuration


@Configuration
//@EnableWebSocketMessageBroker
class WSConfig {
    val log = Log.of(this.javaClass)
//    @Bean
//    fun rsocketMessageHandler() = RSocketMessageHandler().apply {
//        routeMatcher = PathPatternRouteMatcher()
//    }
}