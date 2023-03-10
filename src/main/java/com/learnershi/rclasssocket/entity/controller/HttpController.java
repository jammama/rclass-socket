package com.learnershi.rclasssocket.entity.controller;

import com.learnershi.rclasssocket.entity.ClassRoom;
import com.learnershi.rclasssocket.entity.enums.UserType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HttpController {

    /**
     * 해당 user의 classRoom List를 조회한다.
     *
     * @param userSeq userSeq
     * @param userType userType
     * @return classRoom List
     */
    @RequestMapping("/classRoomList/{userSeq}/{userType}")
    public List<ClassRoom> getClassRoomList(
            @PathVariable String userSeq,
            @PathVariable UserType userType) {
        return null;
    }

}
