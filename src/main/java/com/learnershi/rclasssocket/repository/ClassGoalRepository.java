package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.ClassGoal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ClassGoalRepository extends ReactiveMongoRepository<ClassGoal, String> {
    Flux<ClassGoal> findAllByClassRoomId(String classRoomId);
}
