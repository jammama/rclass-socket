package com.learnershi.rclasssocket.controller;

import com.learnershi.rclasssocket.channel.ClassRoomChannel;
import com.learnershi.rclasssocket.entity.common.Envelop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka Listener -> Socket Channel
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {
    private final ClassRoomChannel classRoomChannel;

    public static final String TOPIC = "user";

    /**
     * Kafka로부터 메세지를 받아
     * ClassRoomChannel을 통해
     * ClassRoom에 연결된 사용자들에게 메세지를 전송한다.
     *
     * @param envelop 메세지 객체
     */
    @KafkaListener(topics = TOPIC)
    public void consume(Envelop envelop) {
        log.info("MessageListener.consume - envelop: {}", envelop);
        classRoomChannel.send(envelop);
    }
}
