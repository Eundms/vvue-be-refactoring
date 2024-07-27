package com.exciting.vvue.auth.jwt.exception;


import com.exciting.vvue.common.exception.ForbiddenException;

public class InvalidTokenException extends ForbiddenException {

    public InvalidTokenException(String reason) {
        super(reason);
    }
}
