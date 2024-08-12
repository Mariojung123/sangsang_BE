package com.example.SecureAndBox.login.exception;

import static com.example.SecureAndBox.login.exception.LoginExceptionCode.*;

public class NotFoundUserInfoException extends CustomException {
	public NotFoundUserInfoException() {
		super(NOT_FOUND_USER_INFO);
	}
}
