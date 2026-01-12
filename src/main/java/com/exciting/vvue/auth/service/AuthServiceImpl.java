package com.exciting.vvue.auth.service;

import com.exciting.vvue.auth.AuthService;
import com.exciting.vvue.auth.jwt.model.JwtDto;
import com.exciting.vvue.auth.jwt.util.JwtUtil;
import com.exciting.vvue.auth.model.Auth;
import com.exciting.vvue.user.model.User;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final JwtUtil jwtUtil;
  private final AuthRepository authRepository;

  @Value("${jwt.access-token-expiration}")
  private long accessTokenExpiration;

  @Value("${jwt.refresh-token-expiration}")
  private long refreshTokenExpiration;

  public AuthServiceImpl(JwtUtil jwtUtil, AuthRepository authRepository) {
    this.jwtUtil = jwtUtil;
    this.authRepository = authRepository;
  }

  @Override
  public JwtDto createTokens(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", user.getId());
    claims.put("nickname", user.getNickname());
    String accessToken = jwtUtil.generateToken("access-token", claims, accessTokenExpiration);
    String refreshToken = jwtUtil.generateToken("refresh-token", claims, refreshTokenExpiration);
    return new JwtDto(user.getId(), accessToken, refreshToken);
  }

  @Override
  public Map<String, Object> getClaimsFromToken(String token) {
    return jwtUtil.getClaims(token);
  }

  @Override
  public Auth getSavedTokenByUserId(Long userId) {
    return authRepository.findById(userId);
  }

  @Override
  public String createAccessToken(Map<String, Object> claims) {
    return jwtUtil.generateToken("access-token", claims, accessTokenExpiration);
  }

  @Override
  public void saveTokens(JwtDto jwtDto) {
    Auth newTokens = Auth.builder()
        .userId(jwtDto.getUserId())
        .refreshToken(jwtDto.getRefreshToken())
        .build();
    authRepository.save(newTokens); // userId가 존재시, update token
  }

  @Override
  public void updateTokens(Auth saved) {
    authRepository.save(saved);
  }

  @Override
  public JwtDto issueTokens(User user) {
    JwtDto jwtDto = createTokens(user);
    Auth authEntity = getSavedTokenByUserId(user.getId());
    if (authEntity == null) {
      saveTokens(jwtDto);
    } else {
      authEntity.setRefreshToken(jwtDto.getRefreshToken());
      updateTokens(authEntity);
    }
    return jwtDto;
  }

}
