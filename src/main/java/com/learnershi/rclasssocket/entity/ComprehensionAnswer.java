package com.learnershi.rclasssocket.entity;

import com.learnershi.rclasssocket.entity.enums.AnswerType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 이해도 설문 : 답안
 */
@Getter
@Document

public class ComprehensionAnswer extends ComprehensionQuestion {

    // 교실 id
    @Setter
    @Accessors(chain = true)
    private String classRoomId;

    // 설문 답안
    private String answer;

    // 사용자 seq
    private String userSeq;

    public ComprehensionAnswer(String id, String classRoomId, String userSeq, String question, String step, AnswerType answerType, String answer) {
        super(question, step, answerType);
        this.setId(id);
        this.userSeq = userSeq;
        this.classRoomId = classRoomId;
        this.answer = answer;
    }

    public ComprehensionAnswer(String id, String userSeq, String question, String step, AnswerType answerType, String answer) {
        super(question, step, answerType);
        this.setId(id);
        this.userSeq = userSeq;
        this.answer = answer;
    }

}
