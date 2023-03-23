package com.learnershi.rclasssocket.controller;

import com.learnershi.rclasssocket.entity.common.Envelop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * 웹소켓 메시지를 보내는 기본 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor()
public abstract class DefaultSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    final String TO_TEACHER = "/teacher/{classRoomId}";
    final String TO_STUDENT = "/student/{classRoomId}";

    /**
     * envelop 내의 receiver 가
     * T 일 때는 /teacher/{classRoomId}로,
     * S 일 때는 /student/{classRoomId}로,
     * A 일 때는 /teacher/{classRoomId}와 /student/{classRoomId}로 메시지를 보낸다.
     *
     * @param envelop 메시지
     */
    void sendToReceiver(Envelop envelop) {
        switch (envelop.getUserType()) {
            case T -> sendToTeacher(envelop.getClassRoomId(), envelop);
            case S -> sendToStudent(envelop.getClassRoomId(), envelop);
            case A -> sendToAll(envelop.getClassRoomId(), envelop);
        }
    }
    void sendToTeacher(String classRoomId, Object data) {
        simpMessagingTemplate.convertAndSend("/teacher/"+ classRoomId, data);
    }

    void sendToStudent(String classRoomId, Object data) {
        simpMessagingTemplate.convertAndSend("/student/"+ classRoomId, data);
    }

    void sendToAll(String classRoomId, Object data) {
        sendToTeacher(classRoomId, data);
        sendToStudent(classRoomId, data);
    }

}
