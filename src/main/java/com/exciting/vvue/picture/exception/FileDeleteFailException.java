package com.exciting.vvue.picture.exception;

import com.exciting.vvue.common.exception.BadRequestException;
import com.exciting.vvue.common.exception.NotFoundException;

public class FileDeleteFailException extends BadRequestException {
	public FileDeleteFailException(String reason){
		super(reason);
	}
}
