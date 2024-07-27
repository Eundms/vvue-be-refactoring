package com.exciting.vvue.auth.exception;


import com.exciting.vvue.common.exception.AuthenticationException;

public class GoogleAuthenticateException extends AuthenticationException {

    public GoogleAuthenticateException(String reason) {
        super(reason);
    }
}
