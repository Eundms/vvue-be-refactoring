package com.exciting.vvue.auth.exception;


import com.exciting.vvue.common.exception.BadRequestException;

public class UserAlreadyExistException extends BadRequestException {

    public UserAlreadyExistException(String reason) {
        super(reason);
    }
}
