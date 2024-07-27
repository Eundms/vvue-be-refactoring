package com.exciting.vvue.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public abstract class ForbiddenException extends VvueApiException {

    public ForbiddenException(String reason) {
        super(HttpStatus.FORBIDDEN, reason);
    }
}