package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "speedQuizSubmit")
public class SpeedQuizSubmit extends ClassRoomEntity {
    private String userName;
    private List testResult;
    private String correctCount;
    private String incorrectCount;
}
