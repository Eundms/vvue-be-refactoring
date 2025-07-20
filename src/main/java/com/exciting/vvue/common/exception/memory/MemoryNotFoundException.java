package com.exciting.vvue.common.exception.memory;

import com.exciting.vvue.common.exception.NotFoundException;

public class MemoryNotFoundException extends NotFoundException {

  public MemoryNotFoundException(String reason) {
    super(reason);
  }
}
