package com.learnershi.rclasssocket.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * 수업 목료
 *
 */
@Document
data class ClassGoal(
    @Id
    var id: String? = null,
    var classRoomId: String,
    var goal: String,
    var done: Boolean
){
    fun setDone(done: Boolean): ClassGoal {
       this.done = done
       return this
    }
}
