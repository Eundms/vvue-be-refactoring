package com.exciting.vvue.auth.model.strategy;

import org.springframework.stereotype.Component;

import com.exciting.vvue.auth.dto.OAuthUserInfoDto;
import com.exciting.vvue.auth.model.OAuthUserInfo;
import com.exciting.vvue.auth.model.GoogleUserInfo;

@Component("GOOGLE")
public class GoogleUserInfoStrategy implements OAuthUserInfoStrategy {
	@Override
	public OAuthUserInfo create(OAuthUserInfoDto dto) {
		return new GoogleUserInfo(dto);
	}
}