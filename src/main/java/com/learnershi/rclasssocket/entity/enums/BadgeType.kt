package com.learnershi.rclasssocket.entity.enums

import java.util.*

enum class BadgeType(val value: String) {
    KNOWLEDGE("knowledge"), SPEED("speed"), COMMUNICATION("communication"), FOCUS("focus"), CHALLENGE("challenge"), HONER(
        "honer"
    ),
    SKILL("skill"), EXPERIENCE("experience"), ANALYZE("analyze"), NONE("none");

    companion object {
        fun findByValue(value: String): BadgeType {
            return Arrays.stream(values())
                .filter { r: BadgeType -> r.value == value }
                .findAny()
                .orElse(NONE)
        }
    }
}