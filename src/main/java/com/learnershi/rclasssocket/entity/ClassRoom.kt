package com.learnershi.rclasssocket.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.learnershi.rclasssocket.entity.enums.*
import lombok.*

/**
 * ClassRoom
 *
 */
class ClassRoom(
    val id: String,
    // 강사 seq
    val teacherSeq: String,
    // 강사 닉네임
    val teacherName: String,
    // 교실 상태
    @JsonProperty("roomState")
    val roomState: ClassState,
    // 교실 제목
    val title: String,
    // 교실 참여자
    @JsonIgnore
    val users: MutableMap<String, User>? = HashMap(),
    // 학습 데이터
    val studyDataMap: MutableMap<Int, StudyData>,
    // 공유 index
    val shareIndex: Int,
    // 학습 모드
    val studyMode: String,
    // 동기화 여부
    val isSync: Boolean,
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

    val students: List<User>
        /**
         * 학생 리스트
         */
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

    @get:JsonIgnore
    val userList: List<User>
        /**
         * 유저 리스트 조회
         *
         * @return 유저 리스트
         */
        get() = ArrayList(users?.values)

    /**
     * 유저 삭제
     *
     * @param memberSeq 식별키
     */
    fun removeUser(memberSeq: String) {
        users?.remove(memberSeq)
    }

    /**
     * StudyData 추가
     *
     * @param studyData 추가
     */
    fun setStudyDataMap(tabIndex: Int?, studyData: StudyData): ClassRoom {
        if (tabIndex != null) {
            studyDataMap[tabIndex] = studyData
        }
        return this
    }
}