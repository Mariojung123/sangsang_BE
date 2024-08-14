package com.example.SecureAndBox.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.example.SecureAndBox.login.exception.CustomException;
import com.example.SecureAndBox.login.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<?> handleCustomException(CustomException exception) {
		return ResponseEntity.status(exception.getHttpStatusCode())
			.body(exception.getErrorMessage().name());
	}

	@ExceptionHandler({
		ArrayIndexOutOfBoundsException.class,
		ArithmeticException.class,
		ClassCastException.class,
		ConcurrentModificationException.class,
		DateTimeParseException.class,
		FileNotFoundException.class,
		HttpMessageNotReadableException.class,
		HttpClientErrorException.BadRequest.class,
		IllegalArgumentException.class,
		IllegalStateException.class,
		InterruptedException.class,
		IOException.class,
		JsonProcessingException.class,
		MethodArgumentNotValidException.class,
		MissingRequestHeaderException.class,
		MissingServletRequestParameterException.class,
		NoSuchElementException.class,
		NoHandlerFoundException.class,
		NullPointerException.class,
		ParseException.class,
		SecurityException.class,
		UnsupportedOperationException.class,
		Exception.class
	})
	protected ResponseEntity<?> handleException(Exception exception) {

		ErrorCode errorCode = determineResponseStatus(exception);

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(exception.getMessage());
	}

	private ErrorCode determineResponseStatus(Exception exception) {
		if (exception instanceof ArrayIndexOutOfBoundsException
			|| exception instanceof DateTimeParseException
			|| exception instanceof FileNotFoundException
			|| exception instanceof InterruptedException
			|| exception instanceof JsonProcessingException
			|| exception instanceof MethodArgumentNotValidException
			|| exception instanceof MissingRequestHeaderException
			|| exception instanceof MissingServletRequestParameterException
			|| exception instanceof NoHandlerFoundException
			|| exception instanceof NullPointerException
			|| exception instanceof ParseException
			|| exception instanceof SecurityException) {
			return ErrorCode.INTERNAL_SERVER_ERROR;
		} else if (exception instanceof ClassCastException
			|| exception instanceof ConcurrentModificationException
			|| exception instanceof HttpMessageNotReadableException
			|| exception instanceof IllegalArgumentException
			|| exception instanceof ArithmeticException
			|| exception instanceof IllegalStateException
			|| exception instanceof IOException
			|| exception instanceof HttpClientErrorException.BadRequest) {
			return ErrorCode.BAD_REQUEST_ERROR;
		} else if (exception instanceof NoSuchElementException
			|| exception instanceof UnsupportedOperationException) {
			return ErrorCode.NOT_FOUND_ERROR;
		} else {
			return ErrorCode.INTERNAL_SERVER_ERROR;
		}
	}
}