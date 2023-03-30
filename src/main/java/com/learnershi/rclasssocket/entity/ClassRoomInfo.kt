package com.learnershi.rclasssocket.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.learnershi.rclasssocket.entity.enums.*
import lombok.*
import lombok.experimental.Accessors
import org.springframework.util.StringUtils

/**
 * ClassRoom
 *
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
class ClassRoomInfo(classRoom: ClassRoom) {
    // 교실 id
    var id: String

    // 강사 seq
    var teacherSeq: String

    // 강사 닉네임
    var teacherName: String

    // 교실 상태
    @JsonProperty("roomState")
    var roomState: ClassState
    var title: String

    init {
        id = classRoom.id
        teacherSeq = classRoom.teacherSeq
        teacherName = classRoom.teacherName
        roomState = classRoom.roomState
        title = if (StringUtils.hasText(classRoom.title)) classRoom.title else "Study Room"
    }

    override fun equals(obj: Any?): Boolean {
        if (obj == null) return false
        if (obj === this) return true
        if (obj !is ClassRoomInfo) return false
        return id == obj.id
    }
}