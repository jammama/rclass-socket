package com.learnershi.rclasssocket.entity

import com.learnershi.rclasssocket.entity.enums.UserState
import org.springframework.lang.Nullable

/**
 * 사용자
 *
 */

class User(
    // 사용자 키
    var seq: String,

    // 사용자 명
    var userName: String? = null,

    // 스크린샷 url
    var screenshotUrl: String? = null,

    // 캡쳐 url
    var captureUrl: String? = null,

    // 스포트라이트 url
    var spotlightUrl: String? = null,

    // 사용자 상태
    var status: UserState? = null,

    // 학습모드
    var studyMode: String? = null,
) {

    constructor(seq: String) : this(seq, null, null, null, null, null, null) {
        this.seq = seq
    }

    constructor(seq: String, @Nullable userName: String?) : this(seq, userName, null, null, null, null, null) {
        this.userName = userName
        this.seq = seq
    }
}