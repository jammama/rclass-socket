package com.learnershi.rclasssocket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

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
public class Reveal {

	@Id
	private String id;

	private String classRoomId;
	// 탭 인덱스
	private Integer tabIndex;
	// 페이지 인덱스
	private Integer pageIndex;

	// 가리기 스티커 목록 데이터
	private List<Map> data;

	// 가리기 스티커 저장된 데이터
	private List<Map> tmpData;

}
