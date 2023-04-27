package com.learnershi.rclasssocket.entity

/**
 * StudyData
 *
 */
class StudyData(
    var name: String? = null,
    var fileName: String? = null,
    var pageIndex: Int = 0,
    var tabIndex: Int = 0,
    var pages: List<String>? = null,
    var quizSubmit: List<QuizSubmit> = emptyList<QuizSubmit>()
) {
    class QuizSubmit {
        private val questionType: String? = null
        private val quizType: String? = null
        private val choices: List<*>? = null
        private val stdName: String? = null
        private val stdAnswer: String? = null
    }
}