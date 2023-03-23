package com.learnershi.rclasssocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Spotlight
 *
 * @author GyeongSik
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class Spotlight extends ClassRoomEntity {
	private String top;
	private String width;
	private String height;
	private String left;
	private String scrollTop;
	private String writerWidth;
	private String writerHeight;
	private String contentHeight;
	private String borderTop;
	private String borderWidth;
	private String borderLeft;
	private String borderHeight;
}
