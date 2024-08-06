package com.example.SecureAndBox.oauth.security.info;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {

	private String accessToken;

	public UserAuthentication(Object principal, Object credentials,
		Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	public UserAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities,
		String accessToken) {
		super(principal, credentials, authorities);
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
