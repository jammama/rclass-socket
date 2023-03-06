package com.learnershi.rclasssocket.entity;

import com.learnershi.rclasssocket.entity.enums.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 클래스룸 내 사용되는 엔티티들의 부모 클래스
 */
@Accessors(chain = true)
@Getter
@Setter
public abstract class ClassRoomEntity {
    private String classRoomId; // 클래스룸 아이디
}
