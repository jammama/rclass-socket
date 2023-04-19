package com.learnershi.rclasssocket.entity

import com.learnershi.rclasssocket.entity.enums.AnswerType
import java.util.*

/**
 * 이해도 설문
 */
open class ComprehensionQuestion(// 설문 내용
    private val question: String?, // 이해도 단위
    private val step: String?, answerType: AnswerType?
) {
    // 설문 id (설문별 랜덤 uuid 생성)
    public var id: String = UUID.randomUUID().toString()

    // 답안 타입
    private val answerType: AnswerType?

    init {
        this.answerType = answerType
    }
}