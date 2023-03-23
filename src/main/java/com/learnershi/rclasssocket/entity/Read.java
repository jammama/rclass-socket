package com.learnershi.rclasssocket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 교재 가리기
 *
 */
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class Read {
	// 지정된 읽기 시간 (초)
	private int readSecond;
}
