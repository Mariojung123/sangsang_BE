package com.example.SecureAndBox.login.exception;

import static com.example.SecureAndBox.login.exception.LoginExceptionCode.*;

public class NotFoundUserInfoException extends CustomException {
	public NotFoundUserInfoException() {
		super(ErrorCode.NOT_FOUND_ERROR);
	}
}
