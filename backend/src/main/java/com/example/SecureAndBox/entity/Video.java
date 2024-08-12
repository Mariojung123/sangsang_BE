package com.example.SecureAndBox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity //엔티티 정의
@Table(name="video") //사용하지 않으면 클래스 이름이 테이블 이름이 됨
@Getter //lombok getter
@Builder
@AllArgsConstructor
public class Video {
	@Id //기본키를 의미. 반드시 기본키를 가져야함.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long videoId;

	private String title;

	private String url;

	// 비디오 가지고 올 때 필요한 정보 추가 했습니다 -의민 -> 이게 맞는지 모르겠네요
	private String imageUrl;

	private String topic;

	private String description;

	@Column(name = "tags", columnDefinition = "json")
	@JdbcTypeCode(SqlTypes.JSON)
	private List<Map<String, String>> tags;

	public List<Map<String, String>> getTags() {
		return tags != null ? Collections.unmodifiableList(tags) : Collections.emptyList();
	}


	public Video() {

	}
}
