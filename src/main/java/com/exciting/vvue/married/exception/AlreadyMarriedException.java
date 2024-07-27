package com.exciting.vvue.married.exception;

import com.exciting.vvue.common.exception.BadRequestException;

public class AlreadyMarriedException extends BadRequestException {
	public AlreadyMarriedException(String reason) {
		super(reason);
	}
}
