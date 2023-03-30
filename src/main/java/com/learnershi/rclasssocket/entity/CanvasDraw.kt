package com.learnershi.rclasssocket.entity

import org.springframework.data.annotation.Id

/**
 * canvas drawing
 *
 *
 */
data class CanvasDraw(
    @Id
    private val id: String,
    private val classRoomId: String,
    private val path: List<*>,
    private val left: String,
    private val top: String,
    private val stroke: String,
    private val strokeWidth: String,
    private val fill: String,
    private val originX: String,
    private val originY: String,
    private val angle: String,
    private val scaleX: String,
    private val scaleY: String,
    private val strokeLineCap: String,
    private val strokeLineJoin: String,
    private val writerWidth: String,
    private val userSeq: String,
    private val tabIndex: Int,
    private val pageIndex: Int,
) {




















}