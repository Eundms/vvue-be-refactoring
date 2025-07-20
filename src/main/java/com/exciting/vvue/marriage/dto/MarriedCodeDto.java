package com.exciting.vvue.marriage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MarriedCodeDto {

  private String marriedCode;

  @Builder
  public MarriedCodeDto(String marriedCode) {
    this.marriedCode = marriedCode;
  }
}
