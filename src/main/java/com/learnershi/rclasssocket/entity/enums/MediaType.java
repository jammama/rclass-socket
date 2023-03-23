package com.learnershi.rclasssocket.entity.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MediaType {
    MP3("mp3"),
    YOUTUBE("youtube"),
    IMAGE("image"),
    NONE("none");

    private final String value;

    MediaType(String value) {
        this.value = value;
    }

    public static MediaType findByValue(String value) {
        return Arrays.stream(MediaType.values())
                .filter(r->r.getValue().equals(value))
                .findAny()
                .orElse(NONE);
    }


}
