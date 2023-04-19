package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.ClassRoomLog
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface ClassRoomLogRepository : ReactiveMongoRepository<ClassRoomLog?, String?> {
    fun findAllByClassRoomId(classRoomId: String?): Flux<ClassRoomLog?>
}