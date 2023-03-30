package com.learnershi.rclasssocket.entity

import org.springframework.data.mongodb.core.mapping.Document

/**
 * Quiz
 *
 */
@Document(collection = "quizShare")
class QuizShare : ClassRoomEntity() {
    private val userName: String? = null
    private val answer: String? = null
}