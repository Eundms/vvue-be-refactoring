package com.exciting.vvue.auth.service;

import com.exciting.vvue.auth.jwt.model.JwtDto;
import com.exciting.vvue.auth.jwt.util.JwtUtil;
import com.exciting.vvue.auth.model.Auth;
import com.exciting.vvue.auth.repository.AuthRepository;
import com.exciting.vvue.user.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private JwtUtil jwtUtil;
    private AuthRepository authRepository;

    public AuthServiceImpl(JwtUtil jwtUtil, AuthRepository authRepository) {
        this.jwtUtil = jwtUtil;
        this.authRepository = authRepository;
    }

    @Override
    public JwtDto createTokens(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        //claims.put("email", user.getEmail());
        claims.put("nickname",user.getNickname());
        String accessToken = jwtUtil.generateToken("access-token", claims, 1000 * 60 * 60 * 1);//1시간
        String refreshToken = jwtUtil.generateToken("refresh-token", claims,
            1000 * 60 * 60 * 10); // DB에 넣어서 관리
        return new JwtDto(user.getId(), accessToken, refreshToken);
    }

    @Override
    public Long getUserIdFromToken(String bearerToken) {
        String token = jwtUtil.removeBearer(bearerToken);
        Map<String, Object> claims = jwtUtil.getClaims(token);
        Long userId = Long.valueOf(claims.get("id").toString());
        return userId;
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
        return jwtUtil.generateToken("access-token", claims, 1000 * 60 * 60 * 1);
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

}

