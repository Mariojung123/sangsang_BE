package com.example.SecureAndBox.exception.problem;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import com.example.SecureAndBox.exception.ExceptionCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum ProblemExceptionCode 	implements ExceptionCode {


		NOT_FOUND_CODE(NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),

		NOT_FOUND_PROBLEM(NOT_FOUND, "해당 문제를 찾을 수 없습니다.");
		private final HttpStatus status;
		private final String message;

		@Override
		public String getCode() {
			return this.name();
		}

}
