package com.learnershi.rclasssocket.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.learnershi.rclasssocket.entity.enums.MessageType
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

/**
 * SSE 메세지에 대한 Log
 *
 */
@Document
class ClassRoomLog(private val classRoomId: String, private val message: MessageType) {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "ko_KR", timezone = "Asia/Seoul")
    private val time: LocalDateTime = LocalDateTime.now()
    private val data: Any? = null

}