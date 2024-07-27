package com.exciting.vvue.auth.exception;


import com.exciting.vvue.common.exception.AuthenticationException;

public class KakaoAuthenticateException extends AuthenticationException {

    public KakaoAuthenticateException(String reason) {
        super(reason);
    }
}
