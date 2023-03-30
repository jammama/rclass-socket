package com.learnershi.rclasssocket.entity.enums

import lombok.Getter
import java.util.*

@Getter
enum class MediaType(val value: String) {
    MP3("mp3"), YOUTUBE("youtube"), IMAGE("image"), NONE("none");

    companion object {
        fun findByValue(value: String): MediaType {
            return Arrays.stream(values())
                .filter { r: MediaType -> r.value == value }
                .findAny()
                .orElse(NONE)
        }
    }
}