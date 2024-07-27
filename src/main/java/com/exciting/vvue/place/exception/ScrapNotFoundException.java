package com.exciting.vvue.place.exception;

import com.exciting.vvue.common.exception.NotFoundException;

public class ScrapNotFoundException extends NotFoundException {
    public ScrapNotFoundException(String reason) {
        super(reason);
    }
}
