package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@Document
public class UserClassRooms {
    @Id
    private String userSeq;         // 사용자 seq
    private LinkedList<ClassRoomInfo> classRooms; // 사용자가 가입한 클래스룸 목록

    public UserClassRooms(){
        if(classRooms == null) {
            classRooms = new LinkedList<>();
        }
    }
    public UserClassRooms(String userSeq, ClassRoomInfo classRoomInfo){
        this();
        this.userSeq = userSeq;
        this.classRooms.push(classRoomInfo);
    }

    /**
     * userClassRooms내 classRoomInfo를 추출한다.
     * 만일 동일 id의 ClassRoomInfo가 없을 경우, 새로운 ClassRoomInfo를 생성하여 반환한다.
     *
     * @param classRoom ClassRoom
     */
    public ClassRoomInfo extractClassRoomInfo(ClassRoom classRoom){
        List<ClassRoomInfo> classRooms = this.getClassRooms();
        ClassRoomInfo classRoomInfo = new ClassRoomInfo(classRoom);

        int index = classRooms.indexOf(classRoomInfo);
        if(index > -1){
            classRoomInfo = classRooms.get(index);
            classRooms.remove(index);
        }
        return classRoomInfo;
    }

}


