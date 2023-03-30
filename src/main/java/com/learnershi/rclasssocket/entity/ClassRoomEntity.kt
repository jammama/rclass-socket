package com.learnershi.rclasssocket.entity

import lombok.Getter
import lombok.Setter
import lombok.experimental.Accessors

/**
 * 클래스룸 내 사용되는 엔티티들의 부모 클래스
 */
@Accessors(chain = true)
@Getter
@Setter
abstract class ClassRoomEntity {
    var classRoomId: String? = null // 클래스룸 아이디
}