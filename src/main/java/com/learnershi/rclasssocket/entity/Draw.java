package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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
public class Draw extends ClassRoomEntity {
    private String id;
    private String color;
    private String opacity;
    private String width;
    private List path;
    private String writerWidth;
}