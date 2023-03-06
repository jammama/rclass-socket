package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Random
 * @author Sunhayng
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class VirtualTools extends ClassRoomEntity {
    private Boolean isVirtualToolOpen;
    private String currentVirtualTool;
}