package com.learnershi.rclasssocket.entity

import org.springframework.data.mongodb.core.mapping.Document

/**
 * Random
 *
 */
@Document(collection = "virtualTools")
class VirtualToolAnimations : ClassRoomEntity() {
    private val index = 0
}