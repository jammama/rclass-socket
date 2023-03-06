package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Quiz
 *
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class Quiz extends ClassRoomEntity {
    private List choices;
    private Boolean isTitleInclude;
    private String questionText;
    private String quizType;

}
