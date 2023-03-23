package com.learnershi.rclasssocket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learnershi.rclasssocket.entity.enums.MiniWindowType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * TimeLine 위에 표시될 miniWindow 활동 정보
 */
@Document
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Activity {

    @Id
    private String id;

    private MiniWindowType miniWindowType;

    public String getMiniWindowType() {
        return miniWindowType.getValue();
    }

    private String classRoomId;
    private Object entity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "ko_KR", timezone = "Asia/Seoul")
    private LocalDateTime endTime;


    public Activity(MiniWindowType miniWindowType, String classRoomId, Object entity) {
        this.miniWindowType = miniWindowType;
        this.classRoomId = classRoomId;
        this.entity = entity;
        this.endTime = LocalDateTime.now();
    }
}
