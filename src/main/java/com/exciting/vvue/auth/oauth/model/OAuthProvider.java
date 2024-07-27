package com.exciting.vvue.auth.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OAuthProvider {
    GOOGLE("google"),
    KAKAO("kakao");

    private String providerName;
}
