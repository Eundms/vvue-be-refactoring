package com.exciting.vvue.memory.dto.res;

import com.exciting.vvue.place.dto.res.PlaceResDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceMemoryToMapDto {

  List<MemoryAtPlaceDto> memoryAtPlaceDtos;
  private PlaceResDto place; // 장소 정보

}
