package com.learnershi.rclasssocket.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Log {
    fun of(clazz : Class<*>) : Logger {
        return LoggerFactory.getLogger(clazz)
    }
}