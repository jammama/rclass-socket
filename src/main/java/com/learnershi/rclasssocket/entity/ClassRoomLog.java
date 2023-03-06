package com.learnershi.rclasssocket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learnershi.rclasssocket.entity.enums.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * SSE 메세지에 대한 Log
 *
 */
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class ClassRoomLog extends ClassRoomEntity {

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime time;
	private MessageType message;
	private Object data;

	public ClassRoomLog(String classRoomId, MessageType message) {
		this.setClassRoomId(classRoomId);
		this.time = LocalDateTime.now();
		this.message = message;
	}}
