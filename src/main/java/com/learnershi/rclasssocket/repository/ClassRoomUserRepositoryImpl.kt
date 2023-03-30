package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.User
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class ClassRoomUserRepositoryImpl : ClassRoomUserRepository {
    init {
        if (classroomUserMap == null) {
            classroomUserMap = ConcurrentHashMap()
        }
    }

    override fun addUser(classRoomId: String, user: User) {
        var userMap = classroomUserMap!![classRoomId]
        if (userMap == null) {
            userMap = ConcurrentHashMap()
        }
        userMap[user.seq] = user
        classroomUserMap!![classRoomId] = userMap
    }

    override fun removeUser(classRoomId: String, userSeq: String): User? {
        val userMap = classroomUserMap!![classRoomId]
        var user: User? = null
        if (userMap != null) {
            user = userMap[userSeq]
            userMap.remove(userSeq)
        }
        return user
    }

    override fun findByClassRoomId(classRoomId: String): Collection<User?> {
        return if (classroomUserMap!![classRoomId] == null) {
            emptySet()
        } else classroomUserMap!![classRoomId]!!.values
    }

    override fun findUserByClassRoomIdAndUserSeq(classRoomId: String, userSeq: String): User? {
        val userMap = classroomUserMap!![classRoomId]
            ?: return null
        return userMap[userSeq]
    }

    companion object {
        private var classroomUserMap: MutableMap<String?, MutableMap<String, User?>?>? = null
    }

}