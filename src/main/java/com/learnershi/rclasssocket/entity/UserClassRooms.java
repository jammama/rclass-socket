package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
public class UserClassRooms {
    private String userSeq;         // 사용자 seq
    private LinkedList<String> classRooms; // 사용자가 가입한 클래스룸 목록

    public UserClassRooms(){
        if(classRooms == null) {
            classRooms = new LinkedList<>();
        }
    }
    public UserClassRooms(String userSeq, String classRoomId){
        this();
        this.userSeq = userSeq;
        this.classRooms.push(classRoomId);
    }

    /**
     * userClassRooms내 classRoomId를 추출한다.
     * 만일 동일 id의 ClassRoomInfo가 없을 경우, ClassRoomId를 다시 반환한다.
     *
     * @param classRoom ClassRoom
     */
    public String extractClassRoomInfo(ClassRoom classRoom){
        List<String> classRooms = this.getClassRooms();

        String classRoomId = classRoom.getId();
        int index = classRooms.indexOf(classRoomId);
        if(index > -1){
            classRooms.remove(index);
        }
        return classRoomId;
    }

}


