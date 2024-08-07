package com.example.SecureAndBox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "user_problem_relation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProblemRelation {


	@Id //기본키를 의미. 반드시 기본키를 가져야함.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne
	@JoinColumn(name = "userId", nullable = false)
	private User user;


	@ManyToOne
	@JoinColumn(name = "problemId", nullable = false)
	private Problem problem;


}
