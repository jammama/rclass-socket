package com.learnershi.rclasssocket.entity.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * TimeLine 내에 저장 될 수 있는 MiniWindow의 종류
 */
@Getter
public enum MiniWindowType {
    QUIZ("quiz"),
    HANDS_UP("handsUp"),
    WORD_TEST("wordTest"),
    RANDOM("random"),
    SPOTLIGHT("spotlight"),
    SCREENSHOT("screenshot"),
    YOUTUBE("youtube"),
    IMAGE("image"),
    MP3("mp3"),
    READ("read"),
    NONE("none");

    private String value;

    MiniWindowType(String value) {
        this.value = value;
    }

    public static MiniWindowType findByValue(String value) {
        return Arrays.stream(MiniWindowType.values())
                .filter(r->r.getValue().equals(value))
                .findAny()
                .orElse(NONE);
    }
    public String getValue() {
        return value;
    }
}
