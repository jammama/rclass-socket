package com.learnershi.rclasssocket.entity.common

import com.learnershi.rclasssocket.entity.enums.MessageType
import com.learnershi.rclasssocket.entity.enums.UserType

/**
 * 웹소켓으로 전달할 Data 객체
 */
data class Envelop(
    val msgType: MessageType,
    val classRoomId: String,
    val userType: UserType,
    val data: Any
) {
}