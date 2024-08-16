package com.example.SecureAndBox.exception.post;

import com.example.SecureAndBox.login.exception.CustomException;

public class NotFoundProblemException extends CustomException {

		public NotFoundProblemException() {
			super(PostExceptionCode.NOT_FOUND_PROBLEM);
		}

}
