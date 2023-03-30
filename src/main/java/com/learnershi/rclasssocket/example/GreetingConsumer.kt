package com.learnershi.rclasssocket.example

import com.learnershi.rclasssocket.entity.common.Envelop
import lombok.RequiredArgsConstructor
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
@RequiredArgsConstructor
class GreetingConsumer(private val simpMessagingTemplate: SimpMessagingTemplate) {

    @KafkaListener(topics = ["greetings"])
    fun consume(envelop: Envelop) {
        simpMessagingTemplate!!.convertAndSend("/greetings", envelop)
    }
}