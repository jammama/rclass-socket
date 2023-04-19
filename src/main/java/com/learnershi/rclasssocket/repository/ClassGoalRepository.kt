package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.ClassGoal
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux


interface ClassGoalRepository : ReactiveMongoRepository<ClassGoal?, String?> {
    fun findAllByClassRoomId(classRoomId: String?): Flux<ClassGoal?>
}