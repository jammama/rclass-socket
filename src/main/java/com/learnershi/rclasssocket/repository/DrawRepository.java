package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.Draw;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface DrawRepository extends ReactiveMongoRepository<Draw, String> {
    Mono<Draw> findByColor(String red);
}
