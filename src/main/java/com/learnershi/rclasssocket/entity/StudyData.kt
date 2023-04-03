package com.learnershi.rclasssocket.entity

/**
 * StudyData
 *
 */
class StudyData {
    private val name: String? = null // 탭 이름
    private val fileName: String? = null // File 이름
    private val pageIndex = 0 // 현재 이미지 index
    val tabIndex = 0 // 현재 탭 index
    private val pages: List<String>? = null // 이미지 경로 List
    private val quizSubmit = emptyList<QuizSubmit>()

    class QuizSubmit {
        private val questionType: String? = null
        private val quizType: String? = null
        private val choices: List<*>? = null
        private val stdName: String? = null
        private val stdAnswer: String? = null
    }
}