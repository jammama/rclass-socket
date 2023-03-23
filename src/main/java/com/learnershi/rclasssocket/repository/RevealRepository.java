package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.Reveal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RevealRepository extends ReactiveMongoRepository<Reveal, String> {
    Mono<Reveal> findByClassRoomIdAndTabIndexAndPageIndex(String classRoomId, Integer tabIndex, Integer pageIndex);
}
