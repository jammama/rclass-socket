package com.learnershi.rclasssocket.config;

import com.learnershi.rclasssocket.controller.MessageListener;
import com.learnershi.rclasssocket.entity.User;
import com.learnershi.rclasssocket.entity.common.Envelop;
import com.learnershi.rclasssocket.entity.enums.MessageType;
import com.learnershi.rclasssocket.entity.enums.UserType;
import com.learnershi.rclasssocket.repository.ClassRoomUserRepository;
import com.learnershi.rclasssocket.repository.SessionUserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Builder
@RequiredArgsConstructor
public class SocketInterceptor implements ChannelInterceptor {

    private final ClassRoomUserRepository classRoomUserRepository;
    private final SessionUserRepository sessionUserRepository;
    private final KafkaTemplate<String, Envelop> kafkaTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String sessionId = accessor.getSessionId();
            String classRoomId = accessor.getNativeHeader("classRoomId").get(0);
            String userSeq = accessor.getNativeHeader("userSeq").get(0);
            String userName = accessor.getNativeHeader("userName").get(0);
            connectUser(sessionId, classRoomId, userSeq, userName);
        }

        else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            String sessionId = accessor.getSessionId();
            disconnectUser(sessionId);
        }

        return message;
    }

    /**
     * 사용자 접속.
     *  1) 세션에 사용자 정보 저장
     *  2) 클래스룸 채널에 사용자 정보 저장
     *  3) 접속 메시지 전송
     *
     * @param sessionId 웹소켓 세션 Id
     * @param classRoomId 클래스룸 Id
     * @param userSeq 사용자 Id
     * @param userName 사용자 이름
     */
    private void connectUser(String sessionId, String classRoomId, String userSeq, String userName) {
        sessionUserRepository.addSession(sessionId, userSeq, classRoomId);

        User newUser = new User(userSeq, userName);
        classRoomUserRepository.addUser(classRoomId, newUser);

        kafkaTemplate.send(MessageListener.TOPIC,
                Envelop.of(MessageType.CONNECT, newUser)
                        .to(UserType.A)
                        .classRoom(classRoomId));
    }

    /**
     * 사용자 접속 해제.
     * 1) 세션에서 사용자 정보 삭제
     * 2) 클래스룸 채널에서 사용자 정보 삭제
     * 3) 접속 해제 메시지 전송
     *
     * @param sessionId 웹소켓 세션 Id
     */
    private void disconnectUser(String sessionId) {
        String classRoomId = sessionUserRepository.getClassRoomId(sessionId);
        String userSeq = sessionUserRepository.getUserSeq(sessionId);

        User removeUser = classRoomUserRepository.removeUser(classRoomId, userSeq);
        sessionUserRepository.removeSession(sessionId);

        kafkaTemplate.send(MessageListener.TOPIC,
                Envelop.of(MessageType.DISCONNECT, removeUser)
                        .to(UserType.A)
                        .classRoom(classRoomId));
    }
}
