package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.Activity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActivityRepository extends ReactiveMongoRepository<Activity, String> {
    Flux<Activity> findAllByClassRoomId(String classRoomId, Pageable pageable);
    Mono<Long> countByClassRoomId(String classRoomId);
}
