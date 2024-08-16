package com.example.SecureAndBox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SecureAndBox.entity.Comment;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
