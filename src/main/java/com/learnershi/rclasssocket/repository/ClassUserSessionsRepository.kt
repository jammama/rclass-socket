package com.learnershi.rclasssocket.repository

interface ClassUserSessionsRepository {
    fun addUserSession(classRoomId: String, userSession: UserSession)
    fun removeUser(classRoomId: String, userSeq: String): UserSession?
    fun findByClassRoomId(classRoomId: String): Collection<UserSession>
    fun findUserByClassRoomIdAndUserSeq(classRoomId: String, userSeq: String): UserSession?
}