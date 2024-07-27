package com.exciting.vvue.auth.oauth.model;


import com.exciting.vvue.auth.oauth.model.dto.OAuthUserInfoDto;

public class GoogleUserInfo implements OAuthUserInfo {

    private final OAuthUserInfoDto oAuthUserInfoDto;

    public GoogleUserInfo(OAuthUserInfoDto userInitialInfo) {
        this.oAuthUserInfoDto = userInitialInfo;
    }

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
