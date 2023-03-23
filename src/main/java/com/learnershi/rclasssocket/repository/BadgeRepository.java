package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.Badge;
import com.learnershi.rclasssocket.entity.enums.BadgeType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BadgeRepository extends ReactiveMongoRepository<Badge, String> {
    Flux<Badge> findAllByClassRoomId(String classRoomId);
    Flux<Badge> findAllByUserSeq(String userSeq);
    Mono<Badge> findByClassRoomIdAndUserSeqAndBadgeType(String classRoomId, String userSeq, BadgeType badgeType);
}
