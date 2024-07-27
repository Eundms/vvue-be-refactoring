package com.exciting.vvue.auth.oauth.model;

public interface OAuthUserInfo {

    String getProviderId();

    String getProvider();

    String getEmail();
    String getNickName();

}
