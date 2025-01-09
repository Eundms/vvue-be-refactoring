package com.exciting.vvue.auth.oauth.model.dto;

import com.exciting.vvue.auth.oauth.model.OAuthProvider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserDto {
	private String email;
	private String nickname;
	private OAuthProvider provider;
	private String providerId;
}
