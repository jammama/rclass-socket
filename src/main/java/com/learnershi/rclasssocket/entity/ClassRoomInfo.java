package com.learnershi.rclasssocket.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learnershi.rclasssocket.entity.enums.ClassState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

/**
 * ClassRoom
 *
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class ClassRoomInfo {

    String id;                  // 교실 id

    String teacherSeq;          // 강사 seq

    String teacherName;     // 강사 닉네임

    @JsonProperty("roomState")
    ClassState roomState;       // 교실 상태

    String title;               // 교실 제목

    public ClassRoomInfo(ClassRoom classRoom){
        this.id = classRoom.getId();
        this.teacherSeq = classRoom.getTeacherSeq();
        this.teacherName = classRoom.getTeacherName();
        this.roomState = classRoom.getRoomState();
        this.title = StringUtils.hasText(classRoom.getTitle()) ? classRoom.getTitle() : "Study Room";
    }


    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        if(obj == this) return true;
        if(!(obj instanceof ClassRoomInfo)) return false;
        ClassRoomInfo classRoomInfo = (ClassRoomInfo) obj;
        return this.id.equals(classRoomInfo.getId());
    }

}
