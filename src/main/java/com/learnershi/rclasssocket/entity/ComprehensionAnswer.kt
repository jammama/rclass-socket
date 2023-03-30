package com.learnershi.rclasssocket.entity

import com.learnershi.rclasssocket.entity.enums.AnswerType
import lombok.Getter
import lombok.Setter
import lombok.experimental.Accessors
import org.springframework.data.mongodb.core.mapping.Document

/**
 * 이해도 설문 : 답안
 */
@Getter
@Document
class ComprehensionAnswer : ComprehensionQuestion {
    // 교실 id
    @Setter
    @Accessors(chain = true)
    private var classRoomId: String? = null

    // 설문 답안
    private var answer: String

    // 사용자 seq
    private var userSeq: String

    constructor(
        id: String,
        classRoomId: String?,
        userSeq: String,
        question: String,
        step: String?,
        answerType: AnswerType?,
        answer: String
    ) : super(id, question, step, answerType) {
        this.id = id
        this.userSeq = userSeq
        this.classRoomId = classRoomId
        this.answer = answer
    }

}