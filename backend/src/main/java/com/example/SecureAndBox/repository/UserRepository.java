package com.example.SecureAndBox.repository;

import java.util.Optional;

import com.example.SecureAndBox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findBySerialId(String s);

	Optional<User> findByUserId(Long userId);

	Optional<User> findByUsername(String userName);

	Optional<User> findByRefreshToken(String refreshToken);
}
