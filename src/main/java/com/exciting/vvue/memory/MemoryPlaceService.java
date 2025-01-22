package com.exciting.vvue.memory;

import java.util.List;

import com.exciting.vvue.memory.dto.MemoryPlaceFindDto;
import com.exciting.vvue.memory.dto.res.PlaceMemoryToMapDto;

public interface MemoryPlaceService {
	List<PlaceMemoryToMapDto> getMemoriablePlaceByMarriedId(Long marriedId, MemoryPlaceFindDto findCondition);
}
