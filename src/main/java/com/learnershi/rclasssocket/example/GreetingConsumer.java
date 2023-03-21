package com.learnershi.rclasssocket.example;


import com.learnershi.rclasssocket.entity.common.Envelop;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GreetingConsumer {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topics = "greetings")
    public void consume(Envelop envelop) {
        simpMessagingTemplate.convertAndSend("/greetings", envelop);
    }
}
