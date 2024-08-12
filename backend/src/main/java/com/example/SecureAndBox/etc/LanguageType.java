package com.example.SecureAndBox.etc;

import lombok.Getter;

@Getter
public enum LanguageType {
	PHP("php"),
	PYTHON("py"),
	CPP("cpp");

	private String key;

	LanguageType(String key) {
		this.key = key;
	}


}
