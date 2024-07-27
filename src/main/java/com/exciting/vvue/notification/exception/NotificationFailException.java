package com.exciting.vvue.notification.exception;

import com.exciting.vvue.common.exception.BadRequestException;

public class NotificationFailException extends BadRequestException {
    public NotificationFailException(String reason) {
        super(reason);
    }
}
