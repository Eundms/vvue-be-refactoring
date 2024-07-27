package com.exciting.vvue.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exciting.vvue.auth.jwt.exception.InvalidTokenException;
import com.exciting.vvue.auth.jwt.model.JwtDto;
import com.exciting.vvue.auth.model.Auth;
import com.exciting.vvue.auth.service.AuthService;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    @Operation(summary = "로그인/회원 가입",description = "토큰 발급 됨")
    @PostMapping
    public ResponseEntity<JwtDto> loginOrRegister(@RequestBody User user) {
        log.debug("[POST] /auth/" + user.getId());

        //        OAuthUserInfo oauthUser = null;
        //        OAuthUserInfoDto userInitialInfo = OAuthUserInfoDto.builder()
        //            .email(socialUser.getEmail()==null?null:socialUser.getEmail())
        //            .providerId(socialUser.getProviderId())
        //            .provider(socialUser.getProvider().getProviderName())
        //            .nickName(socialUser.getNickname())
        //            .build();
        //        if (socialUser.getProvider().equals(OAuthProvider.GOOGLE)) {
        //            oauthUser = new GoogleUserInfo(userInitialInfo);
        //        } else if (socialUser.getProvider().equals(OAuthProvider.KAKAO)) {
        //            oauthUser = new KakaoUserInfo(userInitialInfo);
        //        }
        //
        //        // provider 랑 providerId로 User 있는지 확인
        User userEntity = userService.getUserById(user.getId());
        if (userEntity == null) { // 새로운 유저 -> User 테이블에 저장
            userEntity = userService.createUser(user);
        }
        JwtDto jwtDto = authService.createTokens(userEntity);
        Auth authEntity = authService.getSavedTokenByUserId(userEntity.getId());
        if (authEntity == null) { // 새로운 유저 -> Auth 테이블에 저장
            authService.saveTokens(jwtDto);
        } else { // 기존 유저 -> Auth 테이블의 Token update
            authEntity.setRefreshToken(jwtDto.getRefreshToken());
            authService.updateTokens(authEntity);
        }
        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }
    @Operation(summary = "accessToken 재발급")
    @PostMapping("/refresh-access-token")
    public ResponseEntity<JwtDto> refreshToken(@RequestHeader("refresh-token") String refreshToken)
        throws InvalidTokenException {
        log.debug("[POST] /refresh-access-token " + refreshToken);
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
