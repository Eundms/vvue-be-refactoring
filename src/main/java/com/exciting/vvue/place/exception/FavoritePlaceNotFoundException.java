package com.exciting.vvue.place.exception;

import com.exciting.vvue.common.exception.NotFoundException;

public class FavoritePlaceNotFoundException extends NotFoundException {
	public FavoritePlaceNotFoundException(String reason) {
		super(reason);
	}
}