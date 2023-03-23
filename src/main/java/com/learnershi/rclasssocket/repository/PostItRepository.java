package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.PostIt;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface PostItRepository extends ReactiveMongoRepository<PostIt, String> {
    Mono<PostIt> findByClassRoomIdAndTabIndexAndPageIndex(String classRoomId, Integer tabIndex, Integer pageIndex);
}
