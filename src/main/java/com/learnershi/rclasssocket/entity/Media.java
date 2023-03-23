package com.learnershi.rclasssocket.entity;

import com.learnershi.rclasssocket.entity.enums.MediaType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * TimeLine 위에 표시될 miniWindow 활동 정보
 */
@Document
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Media {

    @Id
    private String id;

    // 미디어 타입
    private MediaType mediaType;

    public String getMediaType() {
        return mediaType.getValue();
    }

    private Object entity;

    public Media(MediaType mediaType, String classRoomId, Object entity) {
        this.mediaType = mediaType;
        this.entity = entity;
    }
}
