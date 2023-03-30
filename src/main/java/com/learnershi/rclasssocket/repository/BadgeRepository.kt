package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.Badge
import com.learnershi.rclasssocket.entity.enums.BadgeType
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface BadgeRepository : ReactiveMongoRepository<Badge?, String?> {
    fun findAllByClassRoomId(classRoomId: String?): Flux<Badge?>
    fun findAllByUserSeq(userSeq: String?): Flux<Badge?>
    fun findByClassRoomIdAndUserSeqAndBadgeType(
        classRoomId: String?,
        userSeq: String?,
        badgeType: BadgeType?
    ): Mono<Badge?>?
}