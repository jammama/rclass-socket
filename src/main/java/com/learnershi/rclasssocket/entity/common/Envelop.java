package com.learnershi.rclasssocket.entity.common;

import com.learnershi.rclasssocket.entity.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 웹소켓으로 전달할 Data의 wrapper
 * 필요 없을듯..
 */
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Envelop {
    private MessageType messageType;
    private String groupId;
    private String receiver;
    private Object data;

}
