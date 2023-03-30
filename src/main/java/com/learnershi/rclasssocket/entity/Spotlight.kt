package com.learnershi.rclasssocket.entity

import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import lombok.experimental.Accessors

/**
 * Spotlight
 *
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
class Spotlight : ClassRoomEntity() {
    private val top: String? = null
    private val width: String? = null
    private val height: String? = null
    private val left: String? = null
    private val scrollTop: String? = null
    private val writerWidth: String? = null
    private val writerHeight: String? = null
    private val contentHeight: String? = null
    private val borderTop: String? = null
    private val borderWidth: String? = null
    private val borderLeft: String? = null
    private val borderHeight: String? = null
}