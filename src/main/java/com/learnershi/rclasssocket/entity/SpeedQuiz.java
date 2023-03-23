package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Speed Quiz
 *
 * @author GyeongSik
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "speedQuiz")
public class SpeedQuiz extends ClassRoomEntity {
    private List speedQuizList;
    private boolean autoSlide;
    private String duration;
}