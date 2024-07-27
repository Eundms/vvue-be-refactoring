package com.exciting.vvue.place.exception;

import com.exciting.vvue.common.exception.VvueApiException;
import org.springframework.http.HttpStatus;

public class PlaceRequiredException extends VvueApiException {
    public PlaceRequiredException(String reason) {
        super(HttpStatus.ACCEPTED, "장소 등록 필요 : " + reason);
    }
}
