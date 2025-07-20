package com.exciting.vvue.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthUserInfoDto {

  private String providerId;//socialId
  private String provider;
  private String email;
  private String nickName;
}
