package com.learnershi.rclasssocket.handler

import com.learnershi.rclasssocket.log.Log

/**
 * Handler의 공통 로직을 담당하는 클래스
 */
open class DefaultHandler {
    private val log = Log.of(this.javaClass)
    fun logInfo(logFormat: String?, vararg objects: Any?) {
        log.info(MARKER_HANDLER, logFormat, objects)
    }

    companion object {
        private const val MARKER_HANDLER = "[HANDLER]"
    }
}