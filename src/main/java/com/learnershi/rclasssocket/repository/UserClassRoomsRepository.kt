package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.UserClassRooms
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserClassRoomsRepository : ReactiveMongoRepository<UserClassRooms?, String?> {
    fun findByUserSeq(userSeq: String?): Mono<UserClassRooms?>
}