package com.learnershi.rclasssocket.repository;

import com.learnershi.rclasssocket.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

}
