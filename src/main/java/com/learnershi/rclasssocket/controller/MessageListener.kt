package com.learnershi.rclasssocket.controller

import com.learnershi.rclasssocket.channel.ClassRoomChannel
import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.log.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.TopicPartition
import org.springframework.stereotype.Component

/**
 * Kafka Listener -> Socket Channel
 */
@Component
@Configuration
class MessageListener(
    private val classRoomChannel: ClassRoomChannel
) {
    private val log = Log.of(this.javaClass)

    @Value("\${spring.kafka.consumer.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Value("\${spring.kafka.consumer.group-id}")
    private lateinit var consumerGroup: String

    /**
     * Kafka로부터 메세지를 받아
     * ClassRoomChannel을 통해
     * ClassRoom에 연결된 사용자들에게 메세지를 전송한다.
     *
     * @param envelop 메세지 객체
     */
    @KafkaListener(topicPartitions = [TopicPartition(topic = TOPIC, partitions = ["0"])])
    fun consume(envelop: Envelop) {
        log.debug("MessageListener.consume - envelop: {}", envelop)
        classRoomChannel.send(envelop)
    }

    companion object {
        const val TOPIC = "user"
    }
}