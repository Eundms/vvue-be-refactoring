package com.exciting.vvue.user.exception;


import com.exciting.vvue.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String reason) {
        super(reason);
    }
}
