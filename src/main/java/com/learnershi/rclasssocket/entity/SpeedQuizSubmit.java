package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * SpeedQuizSubmit
 *
 * @author GyeongSik
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class SpeedQuizSubmit extends ClassRoomEntity {
    private String userName;
    private List testResult;
    private String correctCount;
    private String incorrectCount;
}
