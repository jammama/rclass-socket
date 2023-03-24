package com.learnershi.rclasssocket.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionUserRepository {
    record UserSession(String sessionId, String userSeq, String classRoomId) {}

    private static Map<String, UserSession> userSessionMap;

    public SessionUserRepository() {
        if (userSessionMap == null) {
            userSessionMap = new ConcurrentHashMap<>();
        }
    }

    public void addSession(String sessionId, String userSeq, String classRoomId) {
        userSessionMap.put(sessionId, new UserSession(sessionId, userSeq, classRoomId));
    }

    public void removeSession(String sessionId) {
        userSessionMap.remove(sessionId);
    }

    public String getUserSeq(String sessionId) {
        return userSessionMap.get(sessionId).userSeq;
    }

    public String getClassRoomId(String sessionId) {
        return userSessionMap.get(sessionId).classRoomId;
    }

}
