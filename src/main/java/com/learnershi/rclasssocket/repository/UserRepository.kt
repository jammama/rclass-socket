package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface UserRepository : ReactiveMongoRepository<User?, String?>