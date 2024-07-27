package com.exciting.vvue.place.exception;

import com.exciting.vvue.common.exception.NotFoundException;

public class PlaceStatsNotFoundException extends NotFoundException {
    public PlaceStatsNotFoundException(String reason) {
        super(reason);
    }
}
