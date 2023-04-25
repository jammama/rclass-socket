package com.learnershi.rclasssocket.entity.enums

import java.util.*

/**
 * TimeLine 내에 저장 될 수 있는 MiniWindow의 종류
 */
enum class MiniWindowType(val value: String) {
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

    companion object {
        fun findByValue(value: String): MiniWindowType {
            return Arrays.stream(values())
                .filter { r: MiniWindowType -> r.value == value }
                .findAny()
                .orElse(NONE)
        }
    }
}