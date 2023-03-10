package com.learnershi.rclasssocket.handler;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class MessageHandler {

    private static final String MARKER_HANDLER = "[HANDLER]";

    void logInfo(String logFormat, Object... objects) {
        log.info(MARKER_HANDLER, logFormat, objects);
    }
}
