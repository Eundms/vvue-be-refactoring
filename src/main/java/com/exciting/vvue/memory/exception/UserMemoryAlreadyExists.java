package com.exciting.vvue.memory.exception;

import com.exciting.vvue.common.exception.BadRequestException;

public class UserMemoryAlreadyExists extends BadRequestException {
    public UserMemoryAlreadyExists(String reason) {
        super(reason);
    }
}
