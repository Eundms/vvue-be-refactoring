package com.exciting.vvue.notification.exception;

import com.exciting.vvue.common.exception.NotFoundException;

public class UserNotAddedToNotifyException extends NotFoundException {
    public UserNotAddedToNotifyException(String reason) {
        super(reason);
    }
}
