package com.learnershi.rclasssocket.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Hands-Up
 *
 */
@Document(collection = "draw")
class Draw : ClassRoomEntity() {
    @Id
    private val id: String? = null
    private val color: String? = null
    private val opacity: String? = null
    private val width: String? = null
    private val path: List<*>? = null
    private val writerWidth: String? = null
}