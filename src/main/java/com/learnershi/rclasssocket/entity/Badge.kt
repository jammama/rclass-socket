package com.learnershi.rclasssocket.entity

import com.learnershi.rclasssocket.entity.enums.BadgeType
import org.springframework.data.mongodb.core.mapping.Document

/**
 * 뱃지
 */
@Document
class Badge {
    var id: String? = null

    // 교실 id
    var classRoomId: String? = null

    // 유저 seq
    var userSeq: String? = null

    // 뱃지 타입
    var badgeType: BadgeType = BadgeType.NONE
}