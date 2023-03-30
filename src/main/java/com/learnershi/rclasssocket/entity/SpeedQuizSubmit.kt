package com.learnershi.rclasssocket.entity

import org.springframework.data.mongodb.core.mapping.Document

/**
 * SpeedQuizSubmit
 *
 */
@Document(collection = "speedQuizSubmit")
class SpeedQuizSubmit : ClassRoomEntity() {
    private val userName: String? = null
    private val testResult: List<*>? = null
    private val correctCount: String? = null
    private val incorrectCount: String? = null
}