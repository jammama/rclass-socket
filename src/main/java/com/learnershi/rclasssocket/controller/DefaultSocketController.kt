package com.learnershi.rclasssocket.controller

import com.learnershi.rclasssocket.entity.common.Envelop
import org.springframework.kafka.core.KafkaTemplate

/**
 * 메시지를 보내는 기본 컨트롤러
 */
abstract class DefaultSocketController(private val kafkaTemplate: KafkaTemplate<String?, Envelop?>) {
    val USER_TOPIC: String = MessageListener.TOPIC

    /**
     * envelop 구조의 메시지를 큐로 전송한다.
     *
     * @param envelop 메시지
     */
    fun sendToReceiver(envelop: Envelop?) {
        kafkaTemplate.send(USER_TOPIC, envelop)
    }
}