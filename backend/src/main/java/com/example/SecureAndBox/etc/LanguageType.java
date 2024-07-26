package com.example.SecureAndBox.etc;

import lombok.Getter;

@Getter
public enum LanguageType {
	JAVA("java"),
	JAVASCRIPT("js"),
	CPP("cpp");

	private String key;

	LanguageType(String key) {
		this.key = key;
	}


}
