package com.learnershi.rclasssocket.example;

import com.learnershi.rclasssocket.entity.common.Envelop;
import com.learnershi.rclasssocket.entity.enums.MessageType;
import com.learnershi.rclasssocket.entity.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

// Producer 예제
@Controller
@RequiredArgsConstructor
public class GreetingController {
    private final KafkaTemplate<String, Envelop> kafkaTemplate;

    @MessageMapping("/hello")
    public void sendGreet(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        kafkaTemplate.send("greetings", Envelop.of(MessageType.GREET, message).to(UserType.A).classRoom("1"));
    }

}
