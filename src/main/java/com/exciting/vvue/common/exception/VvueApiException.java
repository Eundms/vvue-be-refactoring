package com.exciting.vvue.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public abstract class VvueApiException extends ResponseStatusException {

    public VvueApiException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
