package com.exciting.vvue.auth.model.strategy;

import com.exciting.vvue.auth.dto.OAuthUserInfoDto;
import com.exciting.vvue.auth.model.KakaoUserInfo;
import com.exciting.vvue.auth.model.OAuthUserInfo;
import org.springframework.stereotype.Component;

@Component("KAKAO")
public class KakaoUserInfoStrategy implements OAuthUserInfoStrategy {

  @Override
  public OAuthUserInfo create(OAuthUserInfoDto dto) {
    return new KakaoUserInfo(dto);
  }
}
