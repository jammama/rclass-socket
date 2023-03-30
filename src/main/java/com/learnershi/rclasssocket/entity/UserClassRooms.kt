package com.learnershi.rclasssocket.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
class UserClassRooms(
    @Id var userSeq: String,
    var classRooms: LinkedList<ClassRoomInfo>,
) {

    /**
     * userClassRooms내 classRoomInfo를 추출한다.
     * 만일 동일 id의 ClassRoomInfo가 없을 경우, 새로운 ClassRoomInfo를 생성하여 반환한다.
     *
     * @param classRoom ClassRoom
     */
    fun extractClassRoomInfo(classRoom: ClassRoom): ClassRoomInfo {
        val classRooms: List<ClassRoomInfo> = this.classRooms
        var classRoomInfo = ClassRoomInfo(classRoom)
        val index = classRooms.indexOf(classRoomInfo)
        if (index > -1) {
            classRoomInfo = classRooms[index]
            classRooms.drop(index)
        }
        return classRoomInfo
    }
}