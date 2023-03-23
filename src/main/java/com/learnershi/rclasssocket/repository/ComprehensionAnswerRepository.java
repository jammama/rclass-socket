package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.ComprehensionAnswer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ComprehensionAnswerRepository extends ReactiveMongoRepository<ComprehensionAnswer, String> {
    Mono<ComprehensionAnswer> findByIdAndUserSeq(String id, String userSeq);
    Flux<ComprehensionAnswer> findAllByClassRoomId(String classRoomId);
}
