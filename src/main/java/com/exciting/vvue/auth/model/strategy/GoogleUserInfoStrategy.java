package com.exciting.vvue.auth.model.strategy;

import com.exciting.vvue.auth.dto.OAuthUserInfoDto;
import com.exciting.vvue.auth.model.GoogleUserInfo;
import com.exciting.vvue.auth.model.OAuthUserInfo;
import org.springframework.stereotype.Component;

@Component("GOOGLE")
public class GoogleUserInfoStrategy implements OAuthUserInfoStrategy {

  @Override
  public OAuthUserInfo create(OAuthUserInfoDto dto) {
    return new GoogleUserInfo(dto);
  }
}