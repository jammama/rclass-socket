package com.learnershi.rclasssocket.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * classRoom 별 studyData
 */
@Document
class ClassRoomStudyDataMap(// ClassRoom Id
    @field:Id private val classRoomId: String
) {
    private var studyDataMap: MutableMap<Int, StudyData>? = null
    fun setStudyData(studyData: StudyData): ClassRoomStudyDataMap {
        if (studyDataMap == null) {
            studyDataMap = HashMap()
        }
        studyDataMap!![studyData.tabIndex] = studyData
        return this
    }
}