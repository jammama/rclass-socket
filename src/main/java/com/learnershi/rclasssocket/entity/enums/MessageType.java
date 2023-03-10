package com.learnershi.rclasssocket.entity.enums;

import lombok.extern.slf4j.Slf4j;
/**
 * MessageType Enum
 *
 * @author Ji Won
 */
@Slf4j
public enum MessageType {

    // ClassRoom
    CONNECT,                // 선생님 연결   -TODO User:(변경된 user)
    DISCONNECT,             // 선생님 끊김   -TODO User:(변경된 user)
    STUDENT_CONNECT,        // 학생 연결    -TODO User:(변경된 user)
    STUDENT_DISCONNECT,     // 학생 끊김    -TODO User:(변경된 user)
    DUPLICATE,              // 중복 연결    -TODO User:(변경된 user)
    REJECT,                 // 강퇴        -TODO User:(변경된 user)
    // SSE

    // Class Room
    PATCH_ROOM,             // Class Room 상태변경 -ClassRoom:classroom TODO: studyData와 classRoom 분리후 해당 메세지 재확인
    USER_UID,               // 사용자 Cam Uid 설정 -(미사용)
    STUDY_MODE,             // 수업 모드 (손들기, 종? 등) -String:studyMode TODO: 왜 string으로 받는지 확인필요
    STUDY_ACTION,           // 수업 모드 (손들기, 종? 등) -User:요청 user
    // Class Room

    // Quiz
    QUIZ_START,             // 퀴즈 시작
    QUIZ_SUBMIT,            // 퀴즈 보내기
    SPEED_QUIZ_START,       // 스피드 퀴즈 시작
    SPEED_QUIZ_SUBMIT, 		// 스피드 퀴즈 제출
    // Quiz

    // WhiteBoard
    DRAW_PATH,				// 선생님이 그린 드로잉 PATH
    DRAW_PATH_LIST,			// 선생님이 그린 드로잉 PATH LIST -(api분리로 미사용)
    // WhiteBoard

    // Sync
    SYNC,                   // 교재 동기화
    UN_SYNC,                // 교재 동기화 해제
    SYNC_CLASSROOM, 		// 학습 동기화
    SYNC_PAGE, 				// 페이지 동기화
    // Sync

    // Share
    SHARE_QUIZ_RESULT,		 // 퀴즈결과 보기
    SHARE_QUIZ_ANSWER,		 // 주관식 같이보기
    // Share

    SHARE_HANDS_UP, 		 // 손들기 같이 보기
    SHARE_RANDOM_PICK, 		 // 랜덤 같이 보기

//    REMOVE_STUDY,
    MINI_WINDOW, 			 // 미니창
    SYNC_MINI_WINDOW, 		 // 현재 미니창
    CLOSE_MINI_WINDOW, 		 // 미니창 닫기

    SHARE_VIRTUAL_TOOLS, 			 // VIRTUAL TOOLS 함께보기
    SHARE_VIRTUAL_TOOL_ANIMATIONS,	// VIRTUAL TOOLS animations

	SPOTLIGHT, 				 // 스포트라이트
	CLOSE_SPOTLIGHT			 // 스포트라이트 닫기
}