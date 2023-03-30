package com.learnershi.rclasssocket.example

import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.entity.enums.*
import lombok.RequiredArgsConstructor
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

// Producer 예제
@Controller
@RequiredArgsConstructor
class GreetingController(private val kafkaTemplate: KafkaTemplate<String, Envelop>) {
    @MessageMapping("/hello")
    @Throws(Exception::class)
    fun sendGreet(message: HelloMessage) {
        Thread.sleep(1000)
        kafkaTemplate.send("greetings", Envelop(msgType = MessageType.GREET, data = message, classRoomId = "test", userType = UserType.S))
    }
}