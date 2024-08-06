package com.example.SecureAndBox.login.exception;

import com.example.SecureAndBox.exception.ExceptionCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final ExceptionCode code;

	public CustomException(ExceptionCode code) {
		super(code.getMessage());
		this.code = code;
	}
}