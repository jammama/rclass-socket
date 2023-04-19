package com.learnershi.rclasssocket.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * canvas drawing
 *
 * @author GyeongSik
 */
@Document(collection = "canvasDraw")
class CanvasDraw {
    @Id
    private val id: String? = null
    private var classRoomId: String? = null
    fun setClassRoomId(classRoomId: String?): CanvasDraw {
        this.classRoomId = classRoomId
        return this
    }

    private val path: List<*>? = null
    private val left: String? = null
    private val top: String? = null
    private val stroke: String? = null
    private val strokeWidth: String? = null
    private val fill: String? = null
    private val originX: String? = null
    private val originY: String? = null
    private val angle: String? = null
    private val scaleX: String? = null
    private val scaleY: String? = null
    private val strokeLineCap: String? = null
    private val strokeLineJoin: String? = null
    private val writerWidth: String? = null
    private val userSeq: String? = null
    private val tabIndex: Int? = null
    private val pageIndex: Int? = null
    private val scrollTop: Int? = null
}