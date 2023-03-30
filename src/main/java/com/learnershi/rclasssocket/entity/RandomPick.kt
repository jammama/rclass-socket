package com.learnershi.rclasssocket.entity

import org.springframework.data.mongodb.core.mapping.Document

/**
 * Random
 *
 */
@Document(collection = "randomPick")
class RandomPick : ClassRoomEntity() {
    private val randomPickList: List<*>? = null
    private val isShuffle: Boolean? = null
    private val pickName: String? = null
}