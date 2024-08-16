package com.example.SecureAndBox.login.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import com.example.SecureAndBox.exception.ExceptionCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginExceptionCode implements ExceptionCode {

	NOT_FOUND_USER_INFO(NOT_FOUND, "유저 정보를 불러올 수 없습니다."),
	INVALID_PASSWORD(BAD_REQUEST, "유효한 비밀번호 형식이 아닙니다."),
	INVALID_USER_ID(BAD_REQUEST, "유효한 아이디 형식이 아닙니다."),
	NOT_MATCH_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
	NOT_MATCH_USERID(BAD_REQUEST, "아이디가 일치하지 않습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getCode() {
		return this.name();
	}
}
