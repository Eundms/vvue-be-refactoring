package com.exciting.vvue.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public abstract class BadRequestException extends VvueApiException {

    public BadRequestException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}