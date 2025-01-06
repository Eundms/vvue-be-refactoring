package com.exciting.vvue.auth.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OAuthProvider {
	GOOGLE("GOOGLE"),
	KAKAO("KAKAO");

	private String providerName;
}
