package com.exciting.vvue.picture.exception;

import com.exciting.vvue.common.exception.BadRequestException;

public class PictureNotFoundException extends BadRequestException {

    public PictureNotFoundException(String reason) {
        super(reason);
    }
}
