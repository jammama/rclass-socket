package com.learnershi.rclasssocket.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * classRoom 별 studyData
 */
@Document
class ClassStudyDataMap(// ClassRoom Id
    @Id var classRoomId: String,
    var studyDataMap: MutableMap<Int, StudyData>? = null
) {
    fun setStudyData(studyData: StudyData): ClassStudyDataMap {
        if (studyDataMap == null) {
            studyDataMap = HashMap()
        }
        studyDataMap!![studyData.tabIndex] = studyData
        return this
    }
}