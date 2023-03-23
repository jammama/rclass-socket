package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.ClassRoomLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ClassRoomLogRepository extends ReactiveMongoRepository<ClassRoomLog, String> {
    Flux<ClassRoomLog>  findAllByClassRoomId(String classRoomId);
}
