package com.learnershi.rclasssocket.entity;

import com.learnershi.rclasssocket.entity.enums.AnswerType;
import lombok.Getter;

import java.util.UUID;

/**
 * 이해도 설문
 */
@Getter
public class ComprehensionQuestion {

    // 설문 id (설문별 랜덤 uuid 생성)
    private String id;

    // 설문 내용
    private final String question;

    // 이해도 단위
    private final String step;

    // 답안 타입
    private final AnswerType answerType;

    protected void setId(String id) {
        this.id = id;
    }

    public ComprehensionQuestion(String question, String step, AnswerType answerType) {
        this.id = UUID.randomUUID().toString();
        this.question = question;
        this.step = step;
        this.answerType = answerType;
    }

}
