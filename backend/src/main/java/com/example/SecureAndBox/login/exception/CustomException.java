package com.example.SecureAndBox.login.exception;

import com.example.SecureAndBox.exception.ExceptionCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorMessage;

	public CustomException(ErrorCode errorMessage) {
		super(String.valueOf(errorMessage.getHttpStatus()));
		this.errorMessage = errorMessage;
	}

	public int getHttpStatusCode() {
		return errorMessage.getHttpStatus().value();
	}
}