package com.exciting.vvue.common.exception.married;

import com.exciting.vvue.common.exception.BadRequestException;

public class MarriedWithSameIdException extends BadRequestException {
	public MarriedWithSameIdException(String reason) {
		super(reason);
	}
}
