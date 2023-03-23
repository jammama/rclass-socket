package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Random
 * @author Sunhayng
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "virtualTools")
public class VirtualToolAnimations extends ClassRoomEntity {
    private int index;
}