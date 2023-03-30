package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.ComprehensionAnswer
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ComprehensionAnswerRepository : ReactiveMongoRepository<ComprehensionAnswer?, String?> {
    fun findByIdAndUserSeq(id: String?, userSeq: String?): Mono<ComprehensionAnswer?>?
    fun findAllByClassRoomId(classRoomId: String?): Flux<ComprehensionAnswer?>
}