package com.example.SecureAndBox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity //엔티티 정의
@Table(name="problem") //사용하지 않으면 클래스 이름이 테이블 이름이 됨
@Getter //lombok getter
@Builder
@AllArgsConstructor
public class Problem {
	@Id //기본키를 의미. 반드시 기본키를 가져야함.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long problemId;

	private String topic;

	private String title;

	private String difficulty;

	public Problem() {

	}
}
