package com.learnershi.rclasssocket.entity

import com.learnershi.rclasssocket.entity.enums.BadgeType
import org.springframework.data.mongodb.core.mapping.Document

/**
 * 뱃지
 */
@Document
data class Badge(
    private val id: String,
    // 교실 id
    private val classRoomId: String,
    // 유저 seq
    private val userSeq: String,
    // 뱃지 타입
    private val badgeType: BadgeType,
)