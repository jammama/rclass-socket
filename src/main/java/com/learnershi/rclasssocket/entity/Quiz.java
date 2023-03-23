package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Quiz
 *
 * @author Jae Deok
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "quiz")
public class Quiz extends ClassRoomEntity {
    private List choices;
    private Boolean isTitleInclude;
    private String questionText;
    private String quizType;

}
