package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.User
import org.springframework.messaging.rsocket.RSocketRequester

data class UserSession(val user: User, val requester: RSocketRequester) {
}