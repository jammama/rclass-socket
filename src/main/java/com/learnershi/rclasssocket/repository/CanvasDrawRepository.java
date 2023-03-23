package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.CanvasDraw;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CanvasDrawRepository extends ReactiveMongoRepository<CanvasDraw, String> {
    Flux<CanvasDraw> findAllByClassRoomId(String classRoomId);

    Flux<CanvasDraw> findAllByClassRoomIdAndUserSeqAndTabIndexAndPageIndex(String classRoomId, String userSeq, Integer tabIndex, Integer pageIndex);

    Flux<CanvasDraw> findAllByClassRoomIdAndUserSeqAndTabIndex(String classRoomId, String userSeq, Integer tabIndex);
}
