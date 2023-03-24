package com.learnershi.rclasssocket.repository;


import com.learnershi.rclasssocket.entity.UserClassRooms;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserClassRoomsRepository extends ReactiveMongoRepository<UserClassRooms, String> {
    Mono<UserClassRooms> findByUserSeq(String userSeq);
}
