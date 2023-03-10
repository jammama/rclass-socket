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
    private MessageType messageType;
    private String classRoomId;
    private UserType receiver;
    private Object data;
}
