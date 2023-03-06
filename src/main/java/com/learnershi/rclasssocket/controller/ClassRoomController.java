package com.learnershi.rclasssocket.controller;

import com.learnershi.rclasssocket.entity.common.Envelop;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/classroom")
@RequiredArgsConstructor
public class ClassRoomController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/test/{classRoomId}")
    public void getMessage(Envelop envelop) {
        simpMessagingTemplate.convertAndSend("/{classRoomId}/"+ envelop.getReceiver(), envelop);
    }
}
