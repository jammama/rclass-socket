package com.learnershi.rclasssocket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learnershi.rclasssocket.entity.enums.ClassState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassRoom
 *
 * @author Ji Won
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class ClassRoom {

    private String id;

    private String teacherSeq;          // 강사 seq

    private String teacherName;     // 강사 닉네임

    @JsonProperty("roomState")
    private ClassState roomState;       // 교실 상태

    private String title;               // 교실 제목
    @JsonIgnore
    Map<String, User> users;            // 교실 참여자

    Map<Integer, StudyData> studyDataMap;          // 학습 데이터

    private int shareIndex;             // 공유 index

    private String studyMode;           // 학습 모드

    private boolean isSync;             // 동기화 여부


    @Builder
    private ClassRoom(String teacherSeq, String teacherName, ClassState state) {
        this.teacherSeq = teacherSeq;
        this.teacherName = teacherName;
        this.roomState = state;
        this.studyDataMap = Collections.emptyMap();
    }

    public ClassRoom modify(ClassRoom classRoom) {
        this.roomState = classRoom.getRoomState();
        this.shareIndex = classRoom.getShareIndex();
        this.studyDataMap = classRoom.getStudyDataMap();
        this.title = classRoom.getTitle();
        return this;
    }

    /**
     * 강사 정보
     */
    @JsonIgnore
    public User getTeacher() {
        if(users == null) {
            return null;
        }
        return users.get(teacherSeq);
    }

    /**
     * 강사 여부
     */
    public boolean isTeacher(String userSeq) {
        return userSeq.equals(teacherSeq);
    }

    /**
     * 학생 리스트
     */
    public List<User> getStudents() {
        if (users == null) {
            return Collections.emptyList();
        }
        return users.values().stream()
                .filter(user -> !user.getSeq().equals(teacherSeq))
                .collect(Collectors.toList());
    }
    
    /**
     * 나를 제외한 유저 정보
     *
     * @param userSeq 유저 순번
     * @return User 정보
     */
    public List<User> getOthers(String userSeq) {
        return users.values().stream().filter(user -> !user.getSeq().equals(userSeq)).collect(Collectors.toList());
    }

    /**
     * 해당 유저 정보
     *
     * @param seq 유저 seq
     * @return User 유저정보
     */
    @JsonIgnore
    public User getUser(String seq) {
        return users.get(seq);
    }

    /**
     * 유저 추가
     *
     * @param user 추가유저
     * @return ClassRoom 해당 교실
     */
    public ClassRoom addUser(User user) {
        if (users == null) {
            users = new HashMap<>();
        }
        users.put(user.getSeq(), user);
        return this;
    }

    /**
     * 유저 리스트 조회
     *
     * @return 유저 리스트
     */
    @JsonIgnore
    public List<User> getUserList() {
        return new ArrayList<>(users.values());
    }
    
    /**
     * 유저 삭제
     *
     * @param memberSeq 식별키
     */
    public void removeUser(String memberSeq) {
        users.remove(memberSeq);
    }

    /**
     * StudyData 추가
     *
     * @param studyData 추가
     */
    public ClassRoom setStudyDataMap(Integer tabIndex, StudyData studyData) {
        if (tabIndex != null) {
            this.studyDataMap.put(tabIndex, studyData);
        }
        return this;
    }

//    /**
//     * classRoom의 학생들에게 메시지를 보낸다.
//     *
//     * @param userChannels 유저 채널
//     * @param message   전송메세지
//     * @param data      전송 데이타
//     */
//    public void sendMessageToStudents(UserChannels userChannels, MessageType message, @Nullable Object data) {
//        sendMessageTo(this.getStudents(), userChannels, message, data);
//    }
//
//    /**
//     * classRoom의 선생님에게 메시지를 보낸다.
//     *
//     * @param userChannels 유저 채널
//     * @param message   전송메세지
//     * @param data      전송 데이타
//     */
//    public void sendMessageToTeacher(UserChannels userChannels, MessageType message, @Nullable Object data) {
//        sendMessageTo(Collections.singletonList(this.getTeacher()), userChannels, message, data);
//    }
//
//    /**
//     * classRoom의 모든 유저에게 메시지를 보낸다.
//     *
//     *  @param userChannels 유저 채널
//     * @param message   전송메세지
//     * @param data      전송 데이타
//     */
//    public void sendMessageToAll(UserChannels userChannels, MessageType message, @Nullable Object data){
//        sendMessageTo(this.getUserList(), userChannels, message, data);
//    }
//
//    /**
//     * classRoom의 유저에게 메시지를 보낸다.
//     * userList내에 teacherSeq가 포함 된 경우, 해당 메세지를 Log로 저장한다.
//     *
//     * @param userChannels 유저 채널
//     * @param message   전송메세지
//     * @param data      전송 데이타
//     */
//    public void sendMessageTo(List<User> userList, UserChannels userChannels, MessageType message, @Nullable Object data){
//        if(userList.contains(this.getTeacher())){
//            userChannels.saveLog(this, message, null);
//        }
//        userList.forEach(user -> userChannels.post(user.getSeq(), JsonUtils.stringify(SseMessage.builder()
//                .name(message)
//                .data(data)
//                .build())));
//    }
}
