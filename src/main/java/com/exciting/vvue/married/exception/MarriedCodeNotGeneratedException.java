package com.exciting.vvue.married.exception;

import com.exciting.vvue.common.exception.NotFoundException;

public class MarriedCodeNotGeneratedException extends NotFoundException {

	public MarriedCodeNotGeneratedException(String reason) {
		super(reason);
	}
}
