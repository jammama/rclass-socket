package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.User

interface ClassRoomUserRepository {
    fun addUser(classRoomId: String, user: User)
    fun removeUser(classRoomId: String, userSeq: String): User?
    fun findByClassRoomId(classRoomId: String): Collection<User?>
    fun findUserByClassRoomIdAndUserSeq(classRoomId: String, userSeq: String): User?
}