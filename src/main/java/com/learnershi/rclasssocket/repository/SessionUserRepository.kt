package com.learnershi.rclasssocket.repository

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class SessionUserRepository {
    @JvmRecord
    internal data class UserSession(val sessionId: String, val userSeq: String, val classRoomId: String)

    init {
        if (userSessionMap == null) {
            userSessionMap = ConcurrentHashMap()
        }
    }

    fun addSession(sessionId: String, userSeq: String, classRoomId: String) {
        userSessionMap!![sessionId] = UserSession(sessionId, userSeq, classRoomId)
    }

    fun removeSession(sessionId: String) {
        userSessionMap!!.remove(sessionId)
    }

    fun getUserSeq(sessionId: String): String {
        return userSessionMap!![sessionId]!!.userSeq
    }

    fun getClassRoomId(sessionId: String): String {
        return userSessionMap!![sessionId]!!.classRoomId
    }

    companion object {
        private var userSessionMap: MutableMap<String, UserSession>? = null
    }
}