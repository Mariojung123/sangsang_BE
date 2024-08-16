package com.example.SecureAndBox.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.SecureAndBox.dto.PostDetailsResponseDto;
import com.example.SecureAndBox.dto.PostRequestDto;
import com.example.SecureAndBox.dto.PostResponseDto;
import com.example.SecureAndBox.entity.Post;
import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final ProblemService problemService;
	public void createPost(PostRequestDto postDto, String description, User user) {

		if(postDto.getParent() != 0) {
			try {
				Problem problem = problemService.getProblemById(postDto.getParent());
			}
			catch(Exception e) {
				throw new IllegalArgumentException("해당 문제가 존재하지 않습니다.");
			}
		}

		Post post  = Post.builder()
				.title(postDto.getTitle())
				.content(description)
				.parent(postDto.getParent())
				.user(user)
				.updatedAt(LocalDateTime.now())
				.build();
		postRepository.save(post);

	}

	public void deletePost(Long postId, User user) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

		if(!post.getUser().getUserId().equals(user.getUserId())) {
			throw new IllegalArgumentException("게시물 작성자만 삭제할 수 있습니다.");
		}

		postRepository.delete(post);
	}

	public PostDetailsResponseDto getpostDetials(Long postId, User user) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

		return PostDetailsResponseDto.builder()
				.title(post.getTitle())
				.postId(post.getPostId())
				.content(post.getContent())
				.username("익명")
				.parent(post.getParent())
				.updatedAt(post.getUpdatedAt())
				.isMe(isMe(user, post))
				.build();

	}

	public List<PostResponseDto> getpost(User user) {
		List<Post> post = postRepository.findAll();

		return post.stream().map(p -> PostResponseDto.builder()
				.title(p.getTitle())
				.postId(p.getPostId())
				.isMe(isMe(user, p))
				.username("익명")
				.parent(p.getParent())
				.updatedAt(p.getUpdatedAt())
				.build()).collect(Collectors.toList());


	}

	public boolean isMe(User user, Post post) {
		return post.getUser().getUserId().equals(user.getUserId());
	}

	public List<PostResponseDto> getpostByParent(User user, Long parent) {
		List<Post> post = postRepository.findByParent(parent);
		if (!post.isEmpty()) {
			return post.stream().map(p -> PostResponseDto.builder()
				.postId(p.getPostId())
				.title(p.getTitle())
				.isMe(isMe(user, p))
				.username("익명")
				.parent(p.getParent())
				.updatedAt(p.getUpdatedAt())
				.build()).collect(Collectors.toList());
		}
		// Return an empty list if no posts are found
		return Collections.emptyList();
	}

	public List<String> getParentPost() {
		return problemService.getProblemId();
	}

	public Post getPost(Long postId) {
		return postRepository.findById(postId).get();
	}
}
