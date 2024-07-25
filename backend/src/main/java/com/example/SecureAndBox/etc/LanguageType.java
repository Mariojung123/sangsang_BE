package com.example.SecureAndBox.etc;

public enum LanguageType {
	JAVA("java"),
	JAVASCRIPT("js"),
	CPP("cpp");

	private String key;

	LanguageType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
