package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Random
 * @author Sunhayng
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "randomPick")
public class RandomPick extends ClassRoomEntity {
    private List randomPickList;  
    private Boolean isShuffle;
    private String pickName;
}