package com.learnershi.rclasssocket.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.learnershi.rclasssocket.entity.enums.MessageType
import java.time.LocalDateTime

/**
 * SSE 메세지에 대한 Log
 *
 */
class ClassRoomLog(classRoomId: String?, message: MessageType) : ClassRoomEntity() {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private val time: LocalDateTime
    private val message: MessageType
    private val data: Any? = null

    init {
        this.classRoomId = classRoomId
        time = LocalDateTime.now()
        this.message = message
    }
}