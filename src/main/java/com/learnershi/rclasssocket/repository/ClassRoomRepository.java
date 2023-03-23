package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.ClassRoom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ClassRoomRepository extends ReactiveMongoRepository<ClassRoom, String> {
}
