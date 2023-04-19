package com.learnershi.rclasssocket.controller

import com.learnershi.rclasssocket.entity.common.Envelop
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

/**
 * 메시지를 보내는 기본 컨트롤러
 */
@Service
class EnvelopSendService(private val kafkaTemplate: KafkaTemplate<String?, Envelop?>) {
    private val USER_TOPIC: String = MessageListener.TOPIC

    /**
     * envelop 구조의 메시지를 큐로 전송한다.
     *
     * @param envelop 메시지
     */
    fun sendMessageQueue(envelop: Envelop?) {
        kafkaTemplate.send(USER_TOPIC, envelop)
    }
}