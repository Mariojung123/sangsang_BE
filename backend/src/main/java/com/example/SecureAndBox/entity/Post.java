package com.example.SecureAndBox.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long postId;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = true)
	private String content;

	@ManyToOne
	@JoinColumn(name = "userId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@LastModifiedDate
	@Column(nullable = true)
	private LocalDateTime updatedAt;

}









