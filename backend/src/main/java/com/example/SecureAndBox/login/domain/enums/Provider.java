package com.example.SecureAndBox.login.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
	KAKAO("KAKAO");

	private final String name;

	@Override
	public String toString() {
		return name;
	}
}