package com.learnershi.rclasssocket.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * classRoom 별 studyData
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@Document
public class ClassRoomStudyDataMap {
    @Id
    private String classRoomId;     // ClassRoom Id

    private Map<Integer, StudyData> studyDataMap;

    public ClassRoomStudyDataMap setStudyData(StudyData studyData){
        if(this.studyDataMap == null){
            this.studyDataMap = new HashMap<>();
        }
        this.studyDataMap.put(studyData.getTabIndex(), studyData);
        return this;
    }

    @Builder
    public ClassRoomStudyDataMap(String classRoomId){
        this.classRoomId = classRoomId;
    }
}