package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Quiz
 *
 * @author Jae Deok
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class QuizShare extends ClassRoomEntity {
    private String userName;
    private String answer;
}
