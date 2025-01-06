package com.exciting.vvue.auth.model.dto;

import com.exciting.vvue.auth.jwt.model.JwtDto;
import com.exciting.vvue.landing.model.LandingStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthRes {
	Long userId; String refreshToken; String accessToken; String stage;
	public AuthRes(Long userId, String refreshToken, String accessToken, String stage) {
		this.userId = userId;
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
		this.stage = stage;
	}

	public static AuthRes from(JwtDto jwtDto, LandingStatus status) {
		return new AuthRes(jwtDto.getUserId(), jwtDto.getRefreshToken(), jwtDto.getAccessToken(), status.toString().toLowerCase());
	}
}