package com.exciting.vvue.auth.model;

import com.exciting.vvue.auth.dto.OAuthUserInfoDto;
import com.exciting.vvue.auth.model.OAuthProvider;
import com.exciting.vvue.auth.model.OAuthUserInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleUserInfo implements OAuthUserInfo {

	private final OAuthUserInfoDto oAuthUserInfoDto;

	@Override
	public String getProviderId() {
		return oAuthUserInfoDto.getProviderId();
	}

	@Override
	public String getProvider() {
		return OAuthProvider.GOOGLE.getProviderName();
	}

	@Override
	public String getEmail() {
		return oAuthUserInfoDto.getEmail();
	}

	@Override
	public String getNickName() {
		return oAuthUserInfoDto.getNickName();
	}

}
