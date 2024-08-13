package com.example.SecureAndBox.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.example.SecureAndBox.dto.VideoRequestDto;
import com.example.SecureAndBox.entity.Video;
import com.example.SecureAndBox.repository.VideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoService {
	private final VideoRepository videoRepository;

	public List<Video> getList(Pageable pageable) {
		return videoRepository.findAll(pageable).getContent();
	}

	public Video getVideo(Long videoId) {
		return videoRepository.findByVideoId(videoId);
	}

	public Video createVideo(VideoRequestDto request) {
		List<Map<String, String>> tagsList = request.getTags().stream()
				.map(tag -> Map.of("variant", tag.getVariant(), "value", tag.getValue()))
				.collect(Collectors.toList());

		Video video = Video.builder()
				.title(request.getTitle())
				.url(request.getUrl())
				.imageUrl(request.getImageUrl())
				.topic(request.getTopic())
				.description(request.getDescription())
				.tags(tagsList)
				.build();
		videoRepository.save(video);
		return video;
	}

	public void deleteVideo(Long videoId) {
		Video video = getVideo(videoId);
		videoRepository.delete(video);
	}
}