package com.learnershi.rclasssocket.entity.common;

import com.learnershi.rclasssocket.entity.enums.MessageType;
import com.learnershi.rclasssocket.entity.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 웹소켓으로 전달할 Data 객체
 */
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Envelop {
    private MessageType msgType;
    private String classRoomId;
    private UserType receiver;
    private Object data;

    /**
     * 메세지 타입과 보낼 내용를 담은 메세지 객체를 생성한다.
     *
     * @param msgType 메세지 타입
     * @param data 보낼 메세지
     * @return Envelop 객체
     */
    public static Envelop of(MessageType msgType, Object data){
        return new Envelop(msgType, null, null, data);
    }

    public Envelop to(UserType userType){
        this.receiver = userType;
        return this;
    }

    public Envelop classRoom(String classRoomId){
        this.classRoomId = classRoomId;
        return this;
    }
}
