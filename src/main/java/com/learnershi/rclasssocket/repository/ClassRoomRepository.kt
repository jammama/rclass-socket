package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.ClassRoom
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ClassRoomRepository : ReactiveMongoRepository<ClassRoom?, String?>