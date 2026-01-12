package com.exciting.vvue.landing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LandingStageResDto {

  private String stage;

  public static LandingStageResDto of(String stage) {
    return new LandingStageResDto(stage);
  }
}
