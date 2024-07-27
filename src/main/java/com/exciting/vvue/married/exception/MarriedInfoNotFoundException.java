package com.exciting.vvue.married.exception;

import com.exciting.vvue.common.exception.NotFoundException;

public class MarriedInfoNotFoundException extends NotFoundException {
	public MarriedInfoNotFoundException(String reason) {
		super(reason);
	}
}
