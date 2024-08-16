package com.example.SecureAndBox.exception.post;

import com.example.SecureAndBox.login.exception.CustomException;

public class NotFoundCommentException extends CustomException {

		public NotFoundCommentException() {
			super(PostExceptionCode.NOT_FOUND_COMMENT);
		}
}
