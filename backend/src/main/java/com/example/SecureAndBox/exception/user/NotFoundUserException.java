package com.example.SecureAndBox.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exception for when a user cannot be found by their ID
@ResponseStatus(HttpStatus.NOT_FOUND) // Sets HTTP status code to 404 Not Found
public class NotFoundUserException extends RuntimeException {

	public NotFoundUserException() {
		super("User not found with the provided ID.");
	}

	public NotFoundUserException(String message) {
		super(message);
	}

	public NotFoundUserException(String message, Throwable cause) {
		super(message, cause);
	}
}