package com.learnershi.rclasssocket.repository

import com.learnershi.rclasssocket.entity.ClassStudyDataMap
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface StudyDataMapRepository : ReactiveMongoRepository<ClassStudyDataMap?, String?>