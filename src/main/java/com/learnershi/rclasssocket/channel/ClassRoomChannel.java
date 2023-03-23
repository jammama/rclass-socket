package com.learnershi.rclasssocket.channel;

import com.learnershi.rclasssocket.entity.User;
import com.learnershi.rclasssocket.entity.common.Envelop;
import com.learnershi.rclasssocket.entity.enums.UserType;
import com.learnershi.rclasssocket.repository.ClassRoomRepository;
import com.learnershi.rclasssocket.repository.ClassRoomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ClassRoom 전송 채널
 */
@Component
@RequiredArgsConstructor
public class ClassRoomChannel {
    private final ClassRoomUserRepository classroomUserRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 소켓 publish 주소
    private final String TO_USER = "/user/%s";

    /**
     * userSeq에 해당하는 사용자에게 envelop을 전송한다.
     *
     * @param userSeq 사용자 seq
     * @param envelop 메세지 객체
     */
    public void sendToUser(String userSeq, Envelop envelop) {
        simpMessagingTemplate.convertAndSend(String.format(TO_USER, userSeq), envelop);
    }

    /**
     * 현재 연결되어있는 사용자 중
     * envelop 내의 ClassRoomId로 classRoom을 조회하고
     * classRoom 내 강사/학생 여부를 분류해
     * 소켓을 통해 envelop을 전송한다.
     *
     * @param envelop 메세지 객체
     */
    public void send(Envelop envelop) {
        String classRoomId = envelop.getClassRoomId();
        getClassRoomUsersByType(classRoomId, envelop.getUserType()).subscribe(users ->
                users.forEach(user ->
                        sendToUser(user.getSeq(), envelop))
        );
    }

    /**
     * 저장 된 classRoom 내의 UserType별로 유저를 분류한다.
     *
     * @param classRoomId classRoomId
     * @param type UserType
     * @return 분류된 유저
     */
    public Mono<Collection<User>> getClassRoomUsersByType(String classRoomId, UserType type) {
        return classRoomRepository.findById(classRoomId)
                .map(classRoom -> {
                    Stream<User> userStream = classroomUserRepository.findByClassRoomId(classRoomId).stream();
                    Stream<User> users = switch (type) {
                        case T -> userStream.filter(user -> classRoom.isTeacher(user.getSeq()));
                        case S -> userStream.filter(user -> !classRoom.isTeacher(user.getSeq()));
                        case A -> userStream;
                    };
                    return users.collect(Collectors.toList());
                }
        );
    }
}
