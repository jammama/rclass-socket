package com.learnershi.rclasssocket.entity

import org.springframework.data.mongodb.core.mapping.Document

/**
 * ImagePages
 *
 */
@Document(collection = "imagePages")
class ImagePages : ClassRoomEntity() {
    private val fileName: String? = null // File 이름
    private val pageIndex = 0 // 현재 이미지 index
    private val pages: List<*>? = null // 이미지 경로 List
}