package com.learnershi.rclasssocket.entity

/**
 * StudyData
 *
 */
class StudyData : ClassRoomEntity() {
    private val name: String? = null // 탭 이름
    private val fileName: String? = null // File 이름
    private val pageIndex = 0 // 현재 이미지 index
    val tabIndex = 0 // 현재 탭 index
    private val pages: List<String>? = null // 이미지 경로 List
    private val quizSubmit = emptyList<QuizSubmit>()
}