package com.learnershi.rclasssocket.entity.controller;

import com.learnershi.rclasssocket.entity.ClassRoom;
import com.learnershi.rclasssocket.entity.StudyData;
import com.learnershi.rclasssocket.entity.User;
import com.learnershi.rclasssocket.entity.common.Envelop;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/classRoom")
public class SocketController extends DefaultSocketController {

    public SocketController(SimpMessagingTemplate simpMessagingTemplate) {
        super(simpMessagingTemplate);
    }

    /**
     * 테스트용: envelop 구조의 메세지를 전송하고 받는다.
     *
     * @param envelop 메세지
     * @return 메세지
     */
    @MessageMapping("/test/{classRoomId}")
    @SendTo({"/student/{classRoomId}", "/teacher/{classRoomId}"})
    public Envelop sendMessage(Envelop envelop) {
        return envelop;
    }

    /**
     * 클래스룸 정보를 수정한다.
     *
     * @param classRoom 클래스룸 정보
     * @return 클래스룸 정보
     */
    @MessageMapping("/{classRoomId}")
    @SendTo({TO_TEACHER, TO_STUDENT})
    public ClassRoom modifyClassRoom(ClassRoom classRoom) {
        return classRoom;
    }

    /**
     * 사용자를 강퇴한다.
     *
     * @param user 사용자 정보
     * @return 사용자 정보
     */
    @MessageMapping("/{classRoomId}/reject")
    @SendTo({TO_TEACHER, TO_STUDENT})
    public User rejectUser(User user) {
        return user;
    }

    /**
     * 클래스룸을 시작한다.
     *
     * @return 메세지
     */
    @MessageMapping("/{classRoomId}/start")
    @SendTo({TO_TEACHER, TO_STUDENT})
    public String startClass() {
        return "start";
    }

    /**
     * 클래스룸을 종료한다.
     *
     * @return 메세지
     */
    @MessageMapping("/{classRoomId}/end")
    @SendTo({TO_TEACHER, TO_STUDENT})
    public String endClass() {
        return "end";
    }

    /**
     * 학습 데이터를 동기화한다.
     *
     * @return 학습 데이터
     */
    @MessageMapping("/{classRoomId}/sync")
    @SendTo({TO_TEACHER, TO_STUDENT})
    public StudyData sync(StudyData studyData) {
        return studyData;
    }

    /**
     * 학습 관련 메세지를 공유한다.
     *
     * @return envelop
     */
    @MessageMapping("/{classRoomId}/studyAction")
    public void studyAction(Envelop envelop) {
        sendToReceiver(envelop);
    }


}
