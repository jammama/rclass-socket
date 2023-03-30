package com.learnershi.rclasssocket.entity

import com.learnershi.rclasssocket.entity.enums.AnswerType
import lombok.Getter
import java.util.*

/**
 * 이해도 설문
 */
@Getter
open class ComprehensionQuestion(// 설문 내용
    var id: String,
    private val question: String?, // 이해도 단위
    private val step: String?, // 답안 타입
    private val answerType: AnswerType?
) {
    // 설문 id (설문별 랜덤 uuid 생성)

    init {
        id = UUID.randomUUID().toString()
    }
}