package com.exciting.vvue.auth.dto.req;

import com.exciting.vvue.auth.model.OAuthProvider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserReqDto {
	private String email;
	private String nickname;
	private OAuthProvider provider;
	private String providerId;
}
