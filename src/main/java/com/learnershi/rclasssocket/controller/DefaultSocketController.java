package com.learnershi.rclasssocket.controller;

import com.learnershi.rclasssocket.entity.common.Envelop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * 메시지를 보내는 기본 컨트롤러
 */
@Slf4j
public abstract class DefaultSocketController {
    private final KafkaTemplate<String, Envelop> kafkaTemplate;

    public DefaultSocketController(KafkaTemplate<String, Envelop> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    final String USER_TOPIC = MessageListener.TOPIC;

    /**
     * envelop 구조의 메시지를 큐로 전송한다.
     *
     * @param envelop 메시지
     */
    void sendToReceiver(Envelop envelop) {
        kafkaTemplate.send(USER_TOPIC, envelop);
    }

}
