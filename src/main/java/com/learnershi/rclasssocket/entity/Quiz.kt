package com.learnershi.rclasssocket.entity

import org.springframework.data.mongodb.core.mapping.Document

/**
 * Quiz
 *
 *
 */
@Document(collection = "quiz")
class Quiz : ClassRoomEntity() {
    private val choices: List<*>? = null
    private val isTitleInclude: Boolean? = null
    private val questionText: String? = null
    private val quizType: String? = null
}