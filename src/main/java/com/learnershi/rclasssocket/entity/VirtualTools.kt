package com.learnershi.rclasssocket.entity

import org.springframework.data.mongodb.core.mapping.Document

/**
 * Random
 * @author Sunhayng
 */
@Document(collection = "virtualTools")
class VirtualTools : ClassRoomEntity() {
    private val isVirtualToolOpen: Boolean? = null
    private val currentVirtualTool: String? = null
}