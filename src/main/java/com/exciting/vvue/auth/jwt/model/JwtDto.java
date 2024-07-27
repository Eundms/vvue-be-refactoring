package com.exciting.vvue.auth.jwt.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtDto {

    private Long userId;
    private String accessToken;
    private String refreshToken;

    @Builder
    public JwtDto(Long userId, String accessToken, String refreshToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
