package com.example.SecureAndBox.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.SecureAndBox.entity.Video;
import com.example.SecureAndBox.repository.VideoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class VideoService {
	private final VideoRepository videoRepository;

	public List<Video> getList(Pageable pageable) {
		return videoRepository.findAll(pageable).getContent();
	}

	public Video getVideo(Long videoId)
	{
		return videoRepository.findByVideoId(videoId);
	}

	public Video createVideo(String title, String url)
	{
		Video video = Video.builder()
			.title(title)
			.url(url)
			.build();
		videoRepository.save(video);
		return video;
	}

	public void deleteVideo(Long videoId)
	{
		Video video = getVideo(videoId);
		videoRepository.delete(video);
	}
}
