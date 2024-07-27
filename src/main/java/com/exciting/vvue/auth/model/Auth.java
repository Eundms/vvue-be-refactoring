package com.exciting.vvue.auth.model;

import lombok.*;

import javax.persistence.Id;

@ToString
@Setter
@Getter
@NoArgsConstructor
public class Auth {

    @Id
    private Long userId;//unique
    private String refreshToken;

    @Builder
    public Auth(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
