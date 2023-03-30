package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.Activity
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ActivityRepository : ReactiveMongoRepository<Activity?, String?> {
    fun findAllByClassRoomId(classRoomId: String?, pageable: Pageable?): Flux<Activity?>
    fun countByClassRoomId(classRoomId: String?): Mono<Long?>
}