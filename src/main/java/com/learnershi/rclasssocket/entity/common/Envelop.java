package com.learnershi.rclasssocket.entity.common;

import com.learnershi.rclasssocket.entity.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 웹소켓으로 전달할 Data의 wrapper
 */
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Envelop {
    private MessageType messageType;
    private String groupId;
    private String receiver;
    private Object data;

    @Builder
    public static Envelop of(MessageType messageType, String groupId, String receiver, Object data) {
        return new Envelop(messageType, groupId, receiver, data);
    }


}
