package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.User;

import java.util.Collection;

public interface ClassRoomUserRepository {

    void addUser(String classRoomId, User user);

    User removeUser(String classRoomId, String userSeq);

    Collection<User> findByClassRoomId(String classRoomId);

    User findUserByClassRoomIdAndUserSeq(String classRoomId, String userSeq);
}
