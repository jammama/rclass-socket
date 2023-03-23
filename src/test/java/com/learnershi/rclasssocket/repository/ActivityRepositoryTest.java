package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.Activity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@ActiveProfiles("local")
class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    private Activity saveBlock;
    private final String testClassRoomId = "repositoryTest";

    @BeforeEach
    void setUp() {
        Activity activity = new Activity();
        activity.setClassRoomId(testClassRoomId);
        saveBlock = activityRepository.save(activity).block();
    }

    @Test
    void findAllByClassRoomId() {
        Pageable pageable = PageRequest.of(0, 10, DESC, "endTime");
        List<Activity> getByTest = activityRepository.findAllByClassRoomId(testClassRoomId, pageable).collectList().block();
        assert getByTest.size() <= 10 && getByTest.size() > 0;
    }

    @Test
    void countByClassRoomId() {
        Long countByTest = activityRepository.countByClassRoomId(testClassRoomId).block();
        assert countByTest > 0;
    }

    @AfterEach
    void tearDown() {
        activityRepository.delete(saveBlock).block();
    }
}