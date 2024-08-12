package com.example.SecureAndBox.login.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Data
public class LoginDto {

	@NotNull(message = "이름은 필수 항목입니다.")
	@Size(min = 2, max = 30, message = "이름은 2자에서 30자 사이여야 합니다.")
	private String username;

	@NotNull
	private String pw;
}
