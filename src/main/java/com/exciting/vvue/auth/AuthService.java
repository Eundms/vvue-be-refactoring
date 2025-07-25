package com.exciting.vvue.auth;

import com.exciting.vvue.auth.jwt.model.JwtDto;
import com.exciting.vvue.auth.model.Auth;
import com.exciting.vvue.user.model.User;
import java.util.Map;

public interface AuthService {

  JwtDto createTokens(User user);

  Map<String, Object> getClaimsFromToken(String token);

  Auth getSavedTokenByUserId(Long id);

  String createAccessToken(Map<String, Object> claims);

  void saveTokens(JwtDto jwtDto);

  void updateTokens(Auth saved);

  JwtDto issueTokens(User saved);
}