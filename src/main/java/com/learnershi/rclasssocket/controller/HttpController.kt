package com.learnershi.rclasssocket.controller

import com.learnershi.rclasssocket.entity.ClassRoom
import com.learnershi.rclasssocket.entity.enums.UserType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HttpController {
    /**
     * 해당 user의 classRoom List를 조회한다.
     *
     * @param userSeq userSeq
     * @param userType userType
     * @return classRoom List
     */
    @RequestMapping("/classRoomList/{userSeq}/{userType}")
    fun getClassRoomList(
        @PathVariable userSeq: String,
        @PathVariable userType: UserType
    ): List<ClassRoom>? {
        return null
    }
}