package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.Reveal
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface RevealRepository : ReactiveMongoRepository<Reveal?, String?> {
    fun findByClassRoomIdAndTabIndexAndPageIndex(classRoomId: String?, tabIndex: Int?, pageIndex: Int?): Mono<Reveal?>
}