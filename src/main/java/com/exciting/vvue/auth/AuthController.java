package com.exciting.vvue.auth;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exciting.vvue.auth.jwt.exception.InvalidTokenException;
import com.exciting.vvue.auth.jwt.model.JwtDto;
import com.exciting.vvue.auth.model.Auth;
import com.exciting.vvue.auth.dto.res.AuthResDto;
import com.exciting.vvue.auth.model.GoogleUserInfo;
import com.exciting.vvue.auth.model.KakaoUserInfo;
import com.exciting.vvue.auth.model.OAuthProvider;
import com.exciting.vvue.auth.model.OAuthUserInfo;
import com.exciting.vvue.auth.dto.req.SocialUserReqDto;
import com.exciting.vvue.auth.dto.OAuthUserInfoDto;
import com.exciting.vvue.common.annotation.NoAuth;
import com.exciting.vvue.landing.LandingService;
import com.exciting.vvue.landing.model.LandingStatus;
import com.exciting.vvue.picture.service.CloudFrontService;
import com.exciting.vvue.user.UserService;
import com.exciting.vvue.user.model.User;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	private final AuthService authService;
	private final LandingService landingService;
	private final CloudFrontService cloudFrontService;
	@Operation(summary = "로그인/회원 가입", description = "토큰 발급 됨")
	@NoAuth
	@PostMapping
	public ResponseEntity<AuthResDto> loginOrRegister(@RequestBody SocialUserReqDto socialUserReqDto,
		HttpServletResponse response) {
		OAuthUserInfo oauthUser = null;
		OAuthUserInfoDto userInitialInfo = OAuthUserInfoDto.builder()
			.email(socialUserReqDto.getEmail() == null ? null : socialUserReqDto.getEmail())
			.providerId(socialUserReqDto.getProviderId())
			.provider(socialUserReqDto.getProvider().getProviderName())
			.nickName(socialUserReqDto.getNickname())
			.build();

		if (socialUserReqDto.getProvider().getProviderName().equals(OAuthProvider.GOOGLE.getProviderName())) {
			oauthUser = new GoogleUserInfo(userInitialInfo);
		} else {
			oauthUser = new KakaoUserInfo(userInitialInfo);
		}

		// provider 랑 providerId로 User 있는지 확인
		User userEntity = userService.getByProviderAndProviderId(oauthUser.getProvider(), oauthUser.getProviderId());
		if (userEntity == null) { // 새로운 유저 -> User 테이블에 저장
			userService.addOAuthUser(oauthUser);
		}
		User saved = userService.getByProviderAndProviderId(oauthUser.getProvider(), oauthUser.getProviderId());
		JwtDto jwtDto = authService.createTokens(saved);
		Auth authEntity = authService.getSavedTokenByUserId(saved.getId());
		if (authEntity == null) { // 새로운 유저 -> Auth 테이블에 저장
			authService.saveTokens(jwtDto);
		} else { // 기존 유저 -> Auth 테이블의 Token update
			authEntity.setRefreshToken(jwtDto.getRefreshToken());
			authService.updateTokens(authEntity);
		}

		Long id = saved.getId();
		LandingStatus status = LandingStatus.from(landingService.getAllRelatedInfo(id));

		// Signed Cookie 생성 및 응답에 추가
		List<String> signedCookies = cloudFrontService.generateSignedCookies();
		for (String cookie : signedCookies) {
			String cookieWithSecurity = cookie + "; SameSite=None; Secure; Domain=.vvue.site; HttpOnly; Path=/";
			response.addHeader(HttpHeaders.SET_COOKIE, cookieWithSecurity);
		}

		return new ResponseEntity<>(AuthResDto.from(jwtDto, status), HttpStatus.OK);
	}


	@Operation(summary = "accessToken 재발급")
	@PostMapping("/refresh-access-token")
	@NoAuth
	public ResponseEntity<JwtDto> refreshToken(@RequestHeader("refresh-token") String refreshToken)
		throws InvalidTokenException {
		Map<String, Object> claims = authService.getClaimsFromToken(refreshToken);
		Auth saved = authService.getSavedTokenByUserId(Long.valueOf(claims.get("id").toString()));
		if (refreshToken.equals(saved.getRefreshToken())) {
			String accessToken = authService.createAccessToken(claims);
			authService.updateTokens(saved);
			JwtDto generated = JwtDto.builder()
				.userId(saved.getUserId())
				.refreshToken(saved.getRefreshToken())
				.accessToken(accessToken)
				.build();
			return new ResponseEntity<>(generated, HttpStatus.OK);
		}
		throw new InvalidTokenException("RefreshToken 이상함");
	}

}