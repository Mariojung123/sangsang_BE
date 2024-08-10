package com.example.SecureAndBox.login.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;


@Data
public class SignUpDto {
	@NotNull(message = "이름은 필수 항목입니다.")
	@Size(min = 2, max = 30, message = "이름은 2자에서 30자 사이여야 합니다.")
	private String username;
	@NotNull
	private String pw;
	@Email(message = "유효한 이메일 주소여야 합니다.")
	private String email;
}
