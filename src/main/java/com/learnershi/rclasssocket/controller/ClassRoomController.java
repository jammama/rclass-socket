package com.learnershi.rclasssocket.controller;

import com.learnershi.rclasssocket.entity.ClassRoom;
import com.learnershi.rclasssocket.entity.User;
import com.learnershi.rclasssocket.entity.common.Envelop;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/classroom")
public class ClassRoomController extends DefaultSocketController {

    public ClassRoomController(SimpMessagingTemplate simpMessagingTemplate) {
        super(simpMessagingTemplate);
    }

    @MessageMapping("/test/{classRoomId}")
    @SendTo({"/student/{classRoomId}", "/teacher/{classRoomId}"})
    public Envelop sendMessage(
            Envelop envelop) {
        return envelop;
    }

    @MessageMapping("/{classRoomId}")
    @SendTo({TO_TEACHER, TO_STUDENT})
    public ClassRoom modifyClassRoom(
            ClassRoom classRoom) {
        return classRoom;
    }

    @MessageMapping("/{classRoomId}/reject")
    @SendTo({TO_TEACHER, TO_STUDENT})
    public User rejectUser(
            User user) {
        return user;
    }

    @MessageMapping("/{classRoomId}/start")
    @SendTo({TO_TEACHER, TO_STUDENT})
    public String startClass() {
        return "start";
    }

    @MessageMapping("/{classRoomId}/end")
    @SendTo({TO_TEACHER, TO_STUDENT})
    public String endClass() {
        return "end";
    }
}
