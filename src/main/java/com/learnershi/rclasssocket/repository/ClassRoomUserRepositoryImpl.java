package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClassRoomUserRepositoryImpl implements ClassRoomUserRepository {
    private static Map<String, Map<String, User>> classroomUserMap;
    private ClassRoomRepository classRoomRepository;

    public ClassRoomUserRepositoryImpl() {
        if (classroomUserMap == null) {
            classroomUserMap = new ConcurrentHashMap<>();
        }
    }

    public void addUser(String classRoomId, User user) {
        Map<String, User> userMap = classroomUserMap.get(classRoomId);
        if (userMap == null) {
            userMap = new ConcurrentHashMap<>();
        }
        userMap.put(user.getSeq(), user);
        classroomUserMap.put(classRoomId, userMap);
    }

    public User removeUser(String classRoomId, String userSeq) {
        Map<String, User> userMap = classroomUserMap.get(classRoomId);
        User user = null;
        if (userMap != null) {
            user = userMap.get(userSeq);
            userMap.remove(userSeq);
        }
        return user;
    }

    public Collection<User> findByClassRoomId(String classRoomId) {
        if (classroomUserMap.get(classRoomId) == null) {
            return Collections.EMPTY_SET;
        }
        return classroomUserMap.get(classRoomId).values();
    }


    public User findUserByClassRoomIdAndUserSeq(String classRoomId, String userSeq) {
        Map<String, User> userMap = classroomUserMap.get(classRoomId);
        if (userMap == null) {
            return null;
        }
        return userMap.get(userSeq);
    }
}
