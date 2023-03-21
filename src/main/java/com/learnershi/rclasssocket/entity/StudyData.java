package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * StudyData
 *
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class StudyData extends ClassRoomEntity {
    private String name;                // 탭 이름
    private String fileName;        // File 이름

    private int pageIndex;          // 현재 이미지 index
    private List<String> pages;             // 이미지 경로 List
    private List<QuizSubmit> quizSubmit = Collections.emptyList();
}