package com.learnershi.rclasssocket.Log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Log {
    val logger : (Any) -> Logger = { LoggerFactory.getLogger(it.javaClass) }
}