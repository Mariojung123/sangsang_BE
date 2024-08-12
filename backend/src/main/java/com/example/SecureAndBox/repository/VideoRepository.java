package com.example.SecureAndBox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
;import com.example.SecureAndBox.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

	Video findByVideoId(Long videoId);
}
