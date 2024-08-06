package com.example.SecureAndBox.login.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import com.example.SecureAndBox.exception.ExceptionCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginExceptionCode implements ExceptionCode {

	NOT_FOUND_USER_INFO(NOT_FOUND, "유저 정보를 불러올 수 없습니다.")
	;

	private final HttpStatus status;
	private final String message;

	@Override
	public String getCode() {
		return this.name();
	}
}
