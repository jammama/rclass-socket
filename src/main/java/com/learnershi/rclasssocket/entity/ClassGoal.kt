package com.learnershi.rclasssocket.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * 수업 목료
 *
 */
@Document
data class ClassGoal(
    @Id
    var id: String? = null,
    val classRoomId: String,
    val goal: String,
    val done: Boolean
)