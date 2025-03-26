package com.exciting.vvue.auth.model.strategy;

import com.exciting.vvue.auth.dto.OAuthUserInfoDto;
import com.exciting.vvue.auth.model.OAuthUserInfo;

public interface OAuthUserInfoStrategy {
	OAuthUserInfo create(OAuthUserInfoDto dto);
}