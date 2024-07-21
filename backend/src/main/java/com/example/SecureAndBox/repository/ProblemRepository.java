package com.example.SecureAndBox.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.SecureAndBox.entity.Problem;

public interface ProblemRepository  extends JpaRepository<Problem, Long> {
	@Query("SELECT p FROM Problem p WHERE p.difficulty = :difficulty")
	List<Problem> findAllByDifficulty(@Param("difficulty") String difficulty);



	@Query("SELECT p FROM Problem p WHERE p.topic = :topic ORDER BY p.difficulty")
	List<Problem> findAllByTopic(@Param("topic") String topic);
}
