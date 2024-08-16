package com.example.SecureAndBox.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.SecureAndBox.login.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ExceptionResponse> handleCustomException(CustomException exception) {
		ExceptionResponse response = ExceptionResponse.from(exception);
		return ResponseEntity.status(response.status()).body(response);
	}
}