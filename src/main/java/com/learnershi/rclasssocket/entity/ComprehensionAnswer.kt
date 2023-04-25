package com.learnershi.rclasssocket.entity

import com.learnershi.rclasssocket.entity.enums.AnswerType
import org.springframework.data.mongodb.core.mapping.Document


/**
 * 이해도 설문 : 답안
 */
@Document
class ComprehensionAnswer(
    val id: String,
    // 교실 id
    var classRoomId: String? = null,
    // 설문 답안
    var answer: String,
    // 사용자 seq
    var userSeq: String,
    var question: String,
    var step: String,
    var answerType: AnswerType

)