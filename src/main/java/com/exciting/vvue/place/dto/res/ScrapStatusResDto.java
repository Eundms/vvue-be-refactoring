package com.exciting.vvue.place.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScrapStatusResDto {

  private Long placeId;
  private boolean scrapped;

  public static ScrapStatusResDto of(Long placeId, boolean scrapped) {
    return new ScrapStatusResDto(placeId, scrapped);
  }
}
