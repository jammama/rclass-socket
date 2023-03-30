package com.learnershi.rclasssocket.controller

import com.learnershi.rclasssocket.entity.common.Envelop
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
@MessageMapping("/classRoom")
class SocketController(kafkaTemplate: KafkaTemplate<String?, Envelop?>) : DefaultSocketController(kafkaTemplate) {
    /**
     * 테스트용: envelop 구조의 메세지를 전송하고 받는다.
     *
     * @param envelop 메세지
     */
    @MessageMapping("/test/{classRoomId}")
    fun sendMessage(envelop: Envelop) {
        sendToReceiver(envelop)
    }

    /**
     * 학습 관련 메세지를 공유한다.
     */
    @MessageMapping("/{classRoomId}/studyAction")
    fun studyAction(envelop: Envelop) {
        sendToReceiver(envelop)
    }
}