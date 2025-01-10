package com.exciting.vvue.auth.model;

import com.exciting.vvue.auth.dto.OAuthUserInfoDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuthUserInfo {

	private final OAuthUserInfoDto oAuthUserInfoDto;

	@Override
	public String getProviderId() {
		return oAuthUserInfoDto.getProviderId();
	}

	@Override
	public String getProvider() {
		return OAuthProvider.KAKAO.getProviderName();
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
