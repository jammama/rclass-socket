package com.learnershi.rclasssocket.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * 교재 가리기
 *
 */
@Document
data class Reveal(
    @Id
    var id: String? = null,
    var classRoomId: String? = null,
    // 탭 인덱스
    var tabIndex: Int? = null,
    // 페이지 인덱스
    var pageIndex: Int? = null,
    // 가리기 스티커 목록 데이터
    var data: List<Map<*, *>>? = null,
    // 가리기 스티커 저장된 데이터
    var tmpData: List<Map<*, *>>? = null
)