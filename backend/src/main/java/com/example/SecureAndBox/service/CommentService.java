package com.example.SecureAndBox.service;

import static com.example.SecureAndBox.exception.post.PostExceptionCode.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.SecureAndBox.dto.CommentRequestDto;
import com.example.SecureAndBox.dto.CommentResponseDto;
import com.example.SecureAndBox.dto.PostResponseDto;
import com.example.SecureAndBox.entity.Comment;
import com.example.SecureAndBox.entity.Post;
import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.exception.post.NotFoundCommentException;
import com.example.SecureAndBox.exception.post.NotFoundPostException;
import com.example.SecureAndBox.login.exception.CustomException;
import com.example.SecureAndBox.repository.CommentRepository;
import com.example.SecureAndBox.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final PostService postService;

	public void createComment(CommentRequestDto commentDto, String description, User user) {
		// 댓글 생성

		if(!postRepository.existsByPostId(commentDto.getPostId())) {
			throw new NotFoundPostException();
		}

		Comment comment = Comment.builder()
				.content(description)
				.user(user)
				.post(Post.builder().postId(commentDto.getPostId()).build())
				.createdAt(LocalDateTime.now())
				.build();
		commentRepository.save(comment);
	}

	public void deleteComment(Long commentId, User user) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(NotFoundCommentException::new);

		if(!comment.getUser().getUserId().equals(user.getUserId())) {
			throw new CustomException(PERMISSION_DENIED);
		}

		commentRepository.delete(comment);
	}

	public List<CommentResponseDto> getComments(Long postId,User user) {

		Post post = postService.getPost(postId);

		// 게시물에 달린 댓글들을 반환
		List<Comment> comments = commentRepository.findAllByPost(post);
		return comments.stream()
				.map(comment -> CommentResponseDto.builder()
						.commentId(comment.getCommentId())
						.content(comment.getContent())
					    .username("익명")
						.postId(comment.getPost().getPostId())
						.isMe(isMe(user, comment))
						.updatedAt(LocalDateTime.now())
						.build())
				.toList();
	}
	boolean isMe(User user, Comment comment) {
		return user.getUserId().equals(comment.getUser().getUserId());
	}
}
