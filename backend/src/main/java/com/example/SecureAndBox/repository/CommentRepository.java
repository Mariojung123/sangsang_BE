package com.example.SecureAndBox.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.SecureAndBox.entity.Comment;
import com.example.SecureAndBox.entity.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


	List<Comment> findAllByPost( Post post);

}
