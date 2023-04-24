package com.learnershi.rclasssocket.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.learnershi.rclasssocket.entity.enums.MiniWindowType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

/**
 * TimeLine 위에 표시될 miniWindow 활동 정보
 */
@Document
data class Activity(
    @Id var id: String? = null,
    var miniWindowType: MiniWindowType,
    var classRoomId: String,
    var entity: Any? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "ko_KR", timezone = "Asia/Seoul")
    var endTime: LocalDateTime = LocalDateTime.now()
) {
    fun getMiniWindowType(): String {
        return miniWindowType.value
    }

}