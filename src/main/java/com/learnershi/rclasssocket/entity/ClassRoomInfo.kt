package com.learnershi.rclasssocket.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.learnershi.rclasssocket.entity.enums.*

/**
 * ClassRoom flat down
 *
 */
class ClassRoomInfo(classRoom: ClassRoom) {
    // 교실 id
    var id: String

    // 강사 seq
    var teacherSeq: String?

    // 강사 닉네임
    var teacherName: String?

    // 교실 상태
    @JsonProperty("roomState")
    var roomState: ClassState?

    var title: String?

    init {
        id = classRoom.id!!
        teacherSeq = classRoom.teacherSeq
        teacherName = classRoom.teacherName
        roomState = classRoom.roomState
        title = classRoom.title ?: "Study Room"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is ClassRoomInfo) return false
        return id == other.id
    }
}