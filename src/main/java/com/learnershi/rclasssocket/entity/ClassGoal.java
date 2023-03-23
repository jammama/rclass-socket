package com.learnershi.rclasssocket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 수업 목료
 *
 */
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class ClassGoal {

	@Id
	private String id;

	private String classRoomId;

	// 목표
	private String goal;

	// 완료 여부
	private boolean done;

}
