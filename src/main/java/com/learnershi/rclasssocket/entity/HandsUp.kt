package com.learnershi.rclasssocket.entity

import org.springframework.data.mongodb.core.mapping.Document

/**
 * Hands-Up
 */
@Document(collection = "handsUp")
class HandsUp : ClassRoomEntity() {
    private val handsUpList: List<*>? = null
}