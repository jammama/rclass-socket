package com.learnershi.rclasssocket.entity.enums


/**
 * MessageType Enum
 *
 */
enum class MessageType {
    // test
    GREET,  // 연결 확인

    // ClassRoom
    CONNECT,
    CLASS_ROOM_INFO,
    PATCH_ROOM,
    STUDENT_CONNECT,
    STUDENT_DISCONNECT,
    DISCONNECT,
    QUIZ_START,
    QUIZ_SUBMIT,
    STUDY_MODE,
    STUDY_ACTION,
    SHARE_QUIZ_RESULT,
    SHARE_QUIZ_ANSWER,
    SHARE_HANDS_UP,
    SHARE_RANDOM_PICK,
    MINI_WINDOW,
    SYNC_MINI_WINDOW,
    CLOSE_MINI_WINDOW,
    SYNC_CLASSROOM,
    SYNC,
    SPEED_QUIZ_START,
    SPEED_QUIZ_SUBMIT,
    DRAW_PATH,
    SHARE_VIRTUAL_TOOLS,
    SHARE_VIRTUAL_TOOL_ANIMATIONS,
    SPOTLIGHT,
    CLOSE_SPOTLIGHT,
    CREATE_CLASS_GOAL,
    UPDATE_CLASS_GOAL,
    DELETE_CLASS_GOAL,
    POST_IT,
    POST_IT_DELETED,
    REVEAL,
    REVEAL_DELETED,
    COMPREHENSION_QUESTION,
    COMPREHENSION_ANSWER,
    MEDIA,
    READ,
    BADGE,
    ERROR
}