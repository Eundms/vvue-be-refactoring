package com.exciting.vvue.user.exception;


import com.exciting.vvue.common.exception.BadRequestException;

public class NicknameAlreadyExistException extends BadRequestException {

    public NicknameAlreadyExistException(String reason) {
        super(reason);
    }
}
