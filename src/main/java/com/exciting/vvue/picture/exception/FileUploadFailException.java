package com.exciting.vvue.picture.exception;


import com.exciting.vvue.common.exception.BadRequestException;

public class FileUploadFailException extends BadRequestException {

    public FileUploadFailException(String reason) {
        super(reason);
    }
}
