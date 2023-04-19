package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.CanvasDraw
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface CanvasDrawRepository : ReactiveMongoRepository<CanvasDraw?, String?> {
    fun findAllByClassRoomId(classRoomId: String?): Flux<CanvasDraw?>
    fun findAllByClassRoomIdAndUserSeqAndTabIndexAndPageIndex(
        classRoomId: String?,
        userSeq: String?,
        tabIndex: Int?,
        pageIndex: Int?
    ): Flux<CanvasDraw?>?

    fun findAllByClassRoomIdAndUserSeqAndTabIndex(
        classRoomId: String?,
        userSeq: String?,
        tabIndex: Int?
    ): Flux<CanvasDraw?>?
}