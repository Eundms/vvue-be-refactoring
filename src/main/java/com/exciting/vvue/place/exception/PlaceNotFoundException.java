package com.exciting.vvue.place.exception;

import com.exciting.vvue.common.exception.NotFoundException;

public class PlaceNotFoundException extends NotFoundException {
    public PlaceNotFoundException(String reason) {
        super(reason);
    }
}
