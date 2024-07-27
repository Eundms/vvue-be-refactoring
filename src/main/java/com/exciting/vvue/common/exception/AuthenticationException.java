package com.exciting.vvue.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public abstract class AuthenticationException extends VvueApiException {

    public AuthenticationException(String reason) {
        super(HttpStatus.UNAUTHORIZED, reason);
    }
}