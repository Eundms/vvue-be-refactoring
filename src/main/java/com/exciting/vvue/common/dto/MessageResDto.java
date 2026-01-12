package com.exciting.vvue.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageResDto {

  private String message;

  public static MessageResDto of(String message) {
    return new MessageResDto(message);
  }
}
