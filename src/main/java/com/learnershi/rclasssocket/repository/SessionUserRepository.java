package com.learnershi.rclasssocket.repository;

import java.util.Map;

public class SessionUserRepository {
    record UserSession(String sessionId, String userSeq, String classRoomId) {}

    private static Map<String, UserSession> userSessionMap;

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