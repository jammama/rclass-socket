package com.learnershi.rclasssocket.controller

import com.learnershi.rclasssocket.channel.ClassRoomChannel
import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.log.Log
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.TopicPartition
import org.springframework.stereotype.Component

/**
 * Kafka Listener -> Socket Channel
 */
@Component
class MessageListener(
    private val classRoomChannel: ClassRoomChannel
) {
    private val log = Log.of(this.javaClass)

    /**
     * Kafka로부터 메세지를 받아
     * ClassRoomChannel을 통해
     * ClassRoom에 연결된 사용자들에게 메세지를 전송한다.
     *
     * @param envelop 메세지 객체
     */
    @KafkaListener(topicPartitions = [TopicPartition(topic = TOPIC, partitions = ["0"])])
    fun consume(envelop: Envelop) {
        log.info("MessageListener.consume - envelop: {}", envelop)
        classRoomChannel.send(envelop)
    }

    companion object {
        const val TOPIC = "user"
    }
}