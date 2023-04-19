package com.learnershi.rclasssocket.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.learnershi.rclasssocket.entity.enums.*
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

/**
 * ClassRoom
 *
 */
data class ClassRoom(
    @Id
    var id: String? = null,
    // 강사 seq
    var teacherSeq: String? = null,
    // 강사 닉네임
    var teacherName: String? = null,
    // 교실 상태
    @JsonProperty("roomState")
    var roomState: ClassState? = ClassState.WAIT,
    // 교실 제목
    var title: String? = null,
    // 교실 참여자
    @JsonIgnore
    var users: MutableMap<String, User>? = HashMap(),
    // 공유 index
    var shareTabIndex: Int = 0,

    var sharePageIndex: Int = 0,

    // 학습 모드
    var studyMode: String? = null,
    // 동기화 여부
    var isSync: Boolean? = null,
    // 시작 시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val startDate: LocalDateTime? = null,
    // 종료 시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var endDate: LocalDateTime? = null,
){
    fun modify(classRoom: ClassRoom): ClassRoom {
        roomState = classRoom.roomState
        this.shareTabIndex = classRoom.shareTabIndex
        this.sharePageIndex = classRoom.sharePageIndex
        title = classRoom.title
        this.isSync = classRoom.isSync
        return this
    }
}