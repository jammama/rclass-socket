package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.ClassRoomStudyDataMap
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ClassRoomStudyDataMapRepository : ReactiveMongoRepository<ClassRoomStudyDataMap?, String?>