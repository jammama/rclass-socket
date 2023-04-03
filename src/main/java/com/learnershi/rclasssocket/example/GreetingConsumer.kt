package com.learnershi.rclasssocket.example

import com.learnershi.rclasssocket.entity.common.Envelop
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class GreetingConsumer(private val simpMessagingTemplate: SimpMessagingTemplate) {

    @KafkaListener(topics = ["greetings"])
    fun consume(envelop: Envelop) {
        simpMessagingTemplate.convertAndSend("/greetings", envelop)
    }
}