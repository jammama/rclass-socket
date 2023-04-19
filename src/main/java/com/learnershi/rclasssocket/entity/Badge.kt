package com.learnershi.rclasssocket.entity

import com.learnershi.rclasssocket.entity.enums.BadgeType
import org.springframework.data.mongodb.core.mapping.Document

/**
 * 뱃지
 */
@Document
class Badge {
    private val id: String? = null

    // 교실 id
    private var classRoomId: String? = null

    // 유저 seq
    private val userSeq: String? = null

    // 뱃지 타입
    private val badgeType: BadgeType = BadgeType.NONE
    fun getBadgeType(): String {
        return badgeType.getValue()
    }

    fun setClassRoomId(classRoomId: String?): Badge {
        this.classRoomId = classRoomId
        return this
    }
}