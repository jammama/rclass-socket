package com.learnershi.rclasssocket.entity;


import com.learnershi.rclasssocket.entity.enums.BadgeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 뱃지
 */
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class Badge {

    private String id;

    // 교실 id
    private String classRoomId;

    // 유저 seq
    private String userSeq;

    // 뱃지 타입
    private BadgeType badgeType;

    public String getBadgeType() {
        return badgeType.getValue();
    }

}
