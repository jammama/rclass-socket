package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("local")
@SpringBootTest
class ClassUserSessionsRepositoryTest {

    @Autowired
    private ClassUserSessionsRepository classroomUsersSession;

    private final String testClassRoomId = "repositoryTestClassRoomId";
    private final String testUserSeq = "testUserSeq";

    @AfterEach
    void tearDownAll() {
        classroomUsersSession.removeUser(testClassRoomId, testUserSeq);
    }

    @Test
    @DisplayName("add/delete user test")
    void removeUser() {
        classroomUsersSession.addUserSession(testClassRoomId, new UserSession(new User(testUserSeq), null));
        assert classroomUsersSession.findUserByClassRoomIdAndUserSeq(testClassRoomId, testUserSeq) != null;

        classroomUsersSession.removeUser(testClassRoomId, testUserSeq);
        assert classroomUsersSession.findUserByClassRoomIdAndUserSeq(testClassRoomId, testUserSeq) == null;
    }

    @Test
    @DisplayName("classRoom내 userList 조회 test")
    void findByClassRoomId() {
        User user = new User(testUserSeq);
        classroomUsersSession.addUserSession(testClassRoomId,  new UserSession(new User(testUserSeq), null));
        Collection<UserSession> classRoomUserList = classroomUsersSession.findByClassRoomId(testClassRoomId);
        assert classRoomUserList.contains(user);
    }

    @Test
    @DisplayName("classRoom내 user 조회 test")
    void findUserByClassRoomIdAndUserSeq() {
        User user = new User(testUserSeq);
        classroomUsersSession.addUserSession(testClassRoomId, new UserSession(new User(testUserSeq), null));
        UserSession findUser = classroomUsersSession.findUserByClassRoomIdAndUserSeq(testClassRoomId, testUserSeq);
        assert findUser.equals(user);
    }
}