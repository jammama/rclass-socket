package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.Memo
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono


interface MemoRepository : ReactiveMongoRepository<Memo?, String?> {
    fun findByClassRoomIdAndTabIndexAndPageIndex(classRoomId: String?, tabIndex: Int?, pageIndex: Int?): Mono<Memo?>
}