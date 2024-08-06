package com.example.SecureAndBox.login.exception;

import static com.server.booyoungee.domain.login.exception.LoginExceptionCode.*;

import com.server.booyoungee.global.exception.CustomException;

public class NotFoundUserInfoException extends CustomException {
	public NotFoundUserInfoException() {
		super(NOT_FOUND_USER_INFO);
	}
}
