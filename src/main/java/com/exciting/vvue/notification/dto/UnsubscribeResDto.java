package com.exciting.vvue.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnsubscribeResDto {

  private boolean unsubscribed;

  public static UnsubscribeResDto of(boolean unsubscribed) {
    return new UnsubscribeResDto(unsubscribed);
  }
}
