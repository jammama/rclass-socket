package com.learnershi.rclasssocket.entity

import com.learnershi.rclasssocket.entity.enums.AnswerType
import java.util.*

/**
 * 이해도 설문
 */
open class ComprehensionQuestion(// 설문 내용
    // 설문 id (설문별 랜덤 uuid 생성)
    open val id: String = UUID.randomUUID().toString(),
    var question: String?, // 이해도 단위
    var step: String?,
    var answerType: AnswerType?
)