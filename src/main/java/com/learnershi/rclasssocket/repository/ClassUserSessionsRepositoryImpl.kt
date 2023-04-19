package com.learnershi.rclasssocket.repository

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class ClassUserSessionsRepositoryImpl : ClassUserSessionsRepository {
    init {
        if (classroomUserMap == null) {
            classroomUserMap = ConcurrentHashMap()
        }
    }

    override fun addUserSession(classRoomId: String, userSession: UserSession) {
        var userMap = classroomUserMap!![classRoomId] ?: ConcurrentHashMap()
        userMap[userSession.user.seq] = userSession
        classroomUserMap!![classRoomId] = userMap
    }

    override fun removeUser(classRoomId: String, userSeq: String): UserSession? {
        val userMap = classroomUserMap!![classRoomId]
        var userSession: UserSession? = null
        if (userMap != null) {
            userSession = userMap[userSeq]
            userMap.remove(userSeq)
        }
        return userSession
    }

    override fun findByClassRoomId(classRoomId: String): Collection<UserSession> {
        return if (classroomUserMap!![classRoomId] == null) {
            emptySet()
        } else classroomUserMap!![classRoomId]!!.values
    }

    override fun findUserByClassRoomIdAndUserSeq(classRoomId: String, userSeq: String): UserSession? {
        return classroomUserMap!![classRoomId]?.get(userSeq)
    }

    companion object {
        private var classroomUserMap: MutableMap<String, MutableMap<String, UserSession>>? = null
    }

}