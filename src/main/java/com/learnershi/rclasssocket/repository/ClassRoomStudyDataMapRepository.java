package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.ClassRoomStudyDataMap;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ClassRoomStudyDataMapRepository extends ReactiveMongoRepository<ClassRoomStudyDataMap, String> {
}
