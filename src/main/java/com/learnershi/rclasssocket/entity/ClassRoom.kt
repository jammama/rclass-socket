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
class ClassRoom(
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
    var shareIndex: Int? = null,
    // 학습 모드
    var studyMode: String? = null,
    // 동기화 여부
    var isSync: Boolean? = null,
    // 시작 시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val startDate: LocalDateTime? = null,
    // 종료 시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private var endDate: LocalDateTime? = null,
) {

    @get:JsonIgnore
    val teacher: User?
        get() = users?.get(teacherSeq)

    /**
     * 강사 여부
     */
    fun isTeacher(userSeq: String): Boolean {
        return userSeq == teacherSeq
    }

    /**
     * 학생 리스트
     */
    val students: List<User>
        get() = users?.values?.filter { user: User -> user.seq != teacherSeq } ?: emptyList()

    /**
     * 나를 제외한 유저 정보
     *
     * @param userSeq 유저 순번
     * @return User 정보
     */
    fun getOthers(userSeq: String): List<User> {
        return users?.values?.filter { user: User -> user.seq != userSeq } ?: emptyList()
    }

    /**
     * 해당 유저 정보
     *
     * @param seq 유저 seq
     * @return User 유저정보
     */
    @JsonIgnore
    fun getUser(seq: String): User? {
        return users?.get(seq)
    }

    /**
     * 유저 추가
     *
     * @param user 추가유저
     * @return ClassRoom 해당 교실
     */
    fun addUser(user: User): ClassRoom {
        users?.put(user.seq, user)
        return this
    }

    /**
     * 유저 리스트 조회
     *
     * @return 유저 리스트
     */
    @get:JsonIgnore
    val userList: List<User>
        get() = ArrayList(users?.values)

    /**
     * 유저 삭제
     *
     * @param memberSeq 식별키
     */
    fun removeUser(memberSeq: String) {
        users?.remove(memberSeq)
    }
}