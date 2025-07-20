package com.exciting.vvue.auth.dto.res;

import com.exciting.vvue.auth.jwt.model.JwtDto;
import com.exciting.vvue.landing.model.LandingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthResDto {

  Long userId;
  String refreshToken;
  String accessToken;
  String stage;

  public AuthResDto(Long userId, String refreshToken, String accessToken, String stage) {
    this.userId = userId;
    this.refreshToken = refreshToken;
    this.accessToken = accessToken;
    this.stage = stage;
  }

  public static AuthResDto from(JwtDto jwtDto, LandingStatus status) {
    return new AuthResDto(jwtDto.getUserId(), jwtDto.getRefreshToken(), jwtDto.getAccessToken(),
        status.toString().toLowerCase());
  }
}