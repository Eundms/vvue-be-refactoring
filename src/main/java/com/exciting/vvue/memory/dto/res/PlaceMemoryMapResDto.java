package com.exciting.vvue.memory.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "추억 지도 응답")
public class PlaceMemoryMapResDto {

  @Schema(description = "장소 개수")
  private int count;

  @Schema(description = "장소별 추억 목록")
  private List<PlaceMemoryToMapDto> placeMemories;

  public static PlaceMemoryMapResDto of(List<PlaceMemoryToMapDto> placeMemories) {
    return new PlaceMemoryMapResDto(placeMemories.size(), placeMemories);
  }
}
