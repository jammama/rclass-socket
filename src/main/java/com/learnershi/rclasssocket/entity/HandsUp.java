package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Hands-Up
 * @author Sunhayng
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "handsUp")
public class HandsUp extends ClassRoomEntity {
    private List handsUpList;
}