package com.exciting.vvue.memory;

import com.exciting.vvue.memory.dto.MemoryPlaceFindDto;
import com.exciting.vvue.memory.dto.res.PlaceMemoryToMapDto;
import java.util.List;

public interface MemoryPlaceService {

  List<PlaceMemoryToMapDto> getMemoriablePlaceByMarriedId(Long marriedId,
      MemoryPlaceFindDto findCondition);
}
