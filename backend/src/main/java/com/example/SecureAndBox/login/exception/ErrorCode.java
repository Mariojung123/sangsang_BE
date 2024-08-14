package com.example.SecureAndBox.login.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST),
	NOT_FOUND_ERROR(HttpStatus.NOT_FOUND),
	NULL_POINT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
	IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
	EMPTY_PRINCIPAL(HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED),
	JWT_IS_EMPTY(HttpStatus.UNAUTHORIZED),
	INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED),
	INVALID_JWT(HttpStatus.UNAUTHORIZED),
	EXPIRED_JWT(HttpStatus.UNAUTHORIZED),
	UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED),
	DUPLICATE_ERROR(HttpStatus.CONFLICT),
	EXCEL_READ_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

	private final HttpStatus httpStatus;

}