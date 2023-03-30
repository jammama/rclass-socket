package com.learnershi.rclasssocket.entity

import org.springframework.data.mongodb.core.mapping.Document

/**
 * Speed Quiz
 *
 */
@Document(collection = "speedQuiz")
class SpeedQuiz : ClassRoomEntity() {
    private val speedQuizList: List<*>? = null
    private val autoSlide = false
    private val duration: String? = null
}