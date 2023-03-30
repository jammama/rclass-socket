package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.Draw
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface DrawRepository : ReactiveMongoRepository<Draw?, String?> {
    fun findByColor(red: String?): Mono<Draw?>?
}