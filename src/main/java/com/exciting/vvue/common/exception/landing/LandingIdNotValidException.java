package com.exciting.vvue.common.exception.landing;

import com.exciting.vvue.common.exception.BadRequestException;

public class LandingIdNotValidException extends BadRequestException {

  public LandingIdNotValidException(String reason) {
    super(reason);
  }
}
