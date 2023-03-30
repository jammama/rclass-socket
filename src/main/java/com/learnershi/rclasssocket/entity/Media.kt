package com.learnershi.rclasssocket.entity

import com.learnershi.rclasssocket.entity.enums.MediaType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * TimeLine 위에 표시될 miniWindow 활동 정보
 */
@Document
class Media(// 미디어 타입
    private val mediaType: MediaType, classRoomId: String?, private val entity: Any
) {
    @Id
    private val id: String? = null
    fun getMediaType(): String {
        return mediaType.value
    }
}