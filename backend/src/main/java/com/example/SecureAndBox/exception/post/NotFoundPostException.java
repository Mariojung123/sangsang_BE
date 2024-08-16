package com.example.SecureAndBox.exception.post;

import com.example.SecureAndBox.login.exception.CustomException;

public class NotFoundPostException extends CustomException {
	public NotFoundPostException() {
		super(PostExceptionCode.NOT_FOUND_POST);
	}



}
