package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * QuizSubmit
 *
 * @author Jae Deok
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class QuizSubmit extends ClassRoomEntity {
    private String questionType;
    private String quizType;
    private List choices;
    private String stdName;
    private String stdAnswer;

}
