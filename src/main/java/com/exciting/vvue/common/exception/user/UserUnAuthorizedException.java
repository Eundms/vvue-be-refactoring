package com.exciting.vvue.common.exception.user;

import com.exciting.vvue.common.exception.UnauthorizedException;

public class UserUnAuthorizedException extends UnauthorizedException {

  public UserUnAuthorizedException(String reason) {
    super(reason);
  }
}
