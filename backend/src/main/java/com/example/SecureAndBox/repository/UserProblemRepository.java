package com.example.SecureAndBox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.entity.UserProblemRelation;

@Repository
public interface UserProblemRepository extends JpaRepository<UserProblemRelation ,Long> {
	List<UserProblemRelation> findAllByUser(User user);


}
