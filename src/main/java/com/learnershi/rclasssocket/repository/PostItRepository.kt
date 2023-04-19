package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.PostIt
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono


interface PostItRepository : ReactiveMongoRepository<PostIt?, String?> {
    fun findByClassRoomIdAndTabIndexAndPageIndex(classRoomId: String?, tabIndex: Int?, pageIndex: Int?): Mono<PostIt?>
}