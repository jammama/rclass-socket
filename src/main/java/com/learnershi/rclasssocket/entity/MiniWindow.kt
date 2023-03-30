package com.learnershi.rclasssocket.entity

import org.springframework.data.mongodb.core.mapping.Document

/**
 * Random
 * @author Sunhayng
 */
@Document(collection = "miniWindow")
class MiniWindow : ClassRoomEntity() {
    private val newMiniWindow: Any? = null
}