package com.exciting.vvue.place.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "즐겨찾기 장소 목록 응답")
public class FavoritePlaceListResDto {

  @Schema(description = "즐겨찾기 장소 개수")
  private int count;

  @Schema(description = "즐겨찾기 장소 목록")
  private List<PlaceResDto> places;

  public static FavoritePlaceListResDto of(List<PlaceResDto> places) {
    return new FavoritePlaceListResDto(places.size(), places);
  }
}
