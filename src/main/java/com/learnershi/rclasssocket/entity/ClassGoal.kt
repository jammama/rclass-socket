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
    private val id: String,
    private val classRoomId: String,
    // 목표
    private val goal: String,
    // 완료 여부
    private val done: Boolean,
)