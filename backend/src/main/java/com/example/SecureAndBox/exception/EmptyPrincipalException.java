package com.example.SecureAndBox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exception for when the Principal is not found
@ResponseStatus(HttpStatus.UNAUTHORIZED) // Sets HTTP status code to 401 Unauthorized
public class EmptyPrincipalException extends RuntimeException {

	public EmptyPrincipalException() {
		super("Principal is missing. The user is not authenticated.");
	}

	public EmptyPrincipalException(String message) {
		super(message);
	}

	public EmptyPrincipalException(String message, Throwable cause) {
		super(message, cause);
	}
}