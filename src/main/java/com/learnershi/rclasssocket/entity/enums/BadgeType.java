package com.learnershi.rclasssocket.entity.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BadgeType {

    KNOWLEDGE("knowledge"),
    SPEED("speed"),
    COMMUNICATION("communication"),
    FOCUS("focus"),
    CHALLENGE("challenge"),
    HONER("honer"),
    SKILL("skill"),
    EXPERIENCE("experience"),
    ANALYZE("analyze"),
    NONE("none");

    final String value;

    BadgeType(String value) {
        this.value = value;
    }

    public static BadgeType findByValue(String value) {
        return Arrays.stream(BadgeType.values())
                .filter(r->r.getValue().equals(value))
                .findAny()
                .orElse(NONE);
    }


}
