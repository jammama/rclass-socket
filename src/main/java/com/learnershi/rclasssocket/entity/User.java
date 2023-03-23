package com.learnershi.rclasssocket.entity;

import com.learnershi.rclasssocket.entity.enums.UserState;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;


/**
 * 사용자
 *
 */
@Accessors(chain = true)
@Getter
@Setter
public class User {
    /**
     * 세션 관리용 사용자키
     */
    @Setter(AccessLevel.NONE)
    String seq;

    /**
     * 사용자 명
     */
    String userName;
    
    /**
     * 스크린샷 url
     */
    String screenshotUrl;

    /**
     * 캡쳐 url
     */
    String captureUrl;

    /**
     * 스포트라이트 url
     */
    String spotlightUrl;

    /**
     * 사용자 상태
     */
    UserState status;

    /**
     * 학습모드
     */
    String studyMode;
    
    public User(String seq){
        this.seq = seq;
    }

    public User(String seq, @Nullable String userName) {
        this.userName = userName;
        this.seq = seq;
    }
}
