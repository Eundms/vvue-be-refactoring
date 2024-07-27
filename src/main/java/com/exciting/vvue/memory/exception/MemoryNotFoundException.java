package com.exciting.vvue.memory.exception;


import com.exciting.vvue.common.exception.NotFoundException;

public class MemoryNotFoundException extends NotFoundException {

    public MemoryNotFoundException(String reason) {
        super(reason);
    }
}
