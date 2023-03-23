package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Hands-Up
 *
 * @author GyeongSik
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "draw")
public class Draw extends ClassRoomEntity {
    @Id
    private String id;
    private String color;
    private String opacity;
    private String width;
    private List path;
    private String writerWidth;
}