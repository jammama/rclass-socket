package com.learnershi.rclasssocket.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * 포스트잇
 *
 */

@Document
data class PostIt(
    @Id
    var id: String? = null,
    var classRoomId: String? = null,
    var tabIndex: Int? = null,
    var pageIndex: Int? = null,
    var data: List<Map<*, *>>? = null
) {
    fun modify(postIt: PostIt): PostIt {
        this.data = postIt.data
        return this
    }
}