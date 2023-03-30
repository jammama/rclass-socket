package com.learnershi.rclasssocket.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * 교재 가리기
 *
 */
@Document
class Reveal {
    @Id
    private val id: String? = null
    private val classRoomId: String? = null

    // 탭 인덱스
    private val tabIndex: Int? = null

    // 페이지 인덱스
    private val pageIndex: Int? = null

    // 가리기 스티커 목록 데이터
    private val data: List<Map<*, *>>? = null

    // 가리기 스티커 저장된 데이터
    private val tmpData: List<Map<*, *>>? = null
}