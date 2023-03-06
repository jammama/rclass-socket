package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ImagePages
 *
 * @author Ji Won
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class ImagePages extends ClassRoomEntity {
    private String fileName;        // File 이름
    private int pageIndex;          // 현재 이미지 index
    private List pages;             // 이미지 경로 List

}
