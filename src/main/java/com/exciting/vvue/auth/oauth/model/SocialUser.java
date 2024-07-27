package com.exciting.vvue.auth.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialUser {
    private String email;
    private String nickname;
    private OAuthProvider provider;
    private String providerId;
}
