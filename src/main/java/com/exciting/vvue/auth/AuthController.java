package com.exciting.vvue.auth;

import com.exciting.vvue.auth.dto.OAuthUserInfoDto;
import com.exciting.vvue.auth.dto.req.SocialUserReqDto;
import com.exciting.vvue.auth.dto.res.AuthResDto;
import com.exciting.vvue.auth.jwt.exception.InvalidTokenException;
import com.exciting.vvue.auth.jwt.model.JwtDto;
import com.exciting.vvue.auth.model.Auth;
import com.exciting.vvue.auth.model.OAuthUserInfo;
import com.exciting.vvue.auth.model.OAuthUserInfoFactory;
import com.exciting.vvue.auth.service.OAuthUserRegistrationService;
import com.exciting.vvue.common.annotation.NoAuth;
import com.exciting.vvue.landing.LandingService;
import com.exciting.vvue.landing.model.LandingStatus;
import com.exciting.vvue.picture.service.CloudFrontService;
import com.exciting.vvue.user.model.User;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final LandingService landingService;
  private final CloudFrontService cloudFrontService;
  private final OAuthUserInfoFactory oauthUserInfoFactory;
  private final OAuthUserRegistrationService oAuthUserRegistrationService;

  @NoAuth
  @PostMapping
  @Override
  public AuthResDto loginOrRegister(@RequestBody SocialUserReqDto socialUserReqDto,
      HttpServletResponse response) {
    OAuthUserInfoDto userInitialInfo = OAuthUserInfoDto.builder()
        .email(socialUserReqDto.getEmail())
        .providerId(socialUserReqDto.getProviderId())
        .provider(socialUserReqDto.getProvider().getProviderName())
        .nickName(socialUserReqDto.getNickname())
        .build();

    OAuthUserInfo oauthUser = oauthUserInfoFactory.create(userInitialInfo);

    User saved = oAuthUserRegistrationService.registerIfNotExists(oauthUser);

    JwtDto jwtDto = authService.issueTokens(saved);

    LandingStatus status = LandingStatus.from(landingService.getAllRelatedInfo(saved.getId()));

    List<String> signedCookies = cloudFrontService.generateSignedCookies();
    for (String cookie : signedCookies) {
      response.addHeader(HttpHeaders.SET_COOKIE, cookie);
    }

    return AuthResDto.from(jwtDto, status);
  }

  @PostMapping("/refresh-access-token")
  @NoAuth
  @Override
  public JwtDto refreshToken(@RequestHeader("refresh-token") String refreshToken)
      throws InvalidTokenException {
    Map<String, Object> claims = authService.getClaimsFromToken(refreshToken);
    Auth saved = authService.getSavedTokenByUserId(Long.valueOf(claims.get("id").toString()));
    if (refreshToken.equals(saved.getRefreshToken())) {
      String accessToken = authService.createAccessToken(claims);
      authService.updateTokens(saved);
      return JwtDto.builder()
          .userId(saved.getUserId())
          .refreshToken(saved.getRefreshToken())
          .accessToken(accessToken)
          .build();
    }
    throw new InvalidTokenException("RefreshToken 이상함");
  }
}
