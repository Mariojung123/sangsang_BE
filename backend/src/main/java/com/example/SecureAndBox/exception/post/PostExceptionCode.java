package com.example.SecureAndBox.exception.post;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.example.SecureAndBox.exception.ExceptionCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostExceptionCode implements ExceptionCode {

	NOT_FOUND_POST(NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),
	NOT_FOUND_COMMENT(NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
	NOT_FOUND_PROBLEM(NOT_FOUND, "해당 문제를 찾을 수 없습니다."),
	PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getCode() {
		return this.name();
	}
}
