package com.exciting.vvue.memory;

import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.memory.model.dto.MemoryPlaceFindDto;

import java.util.List;

public interface MemoryPlaceService {
	List<PlaceMemory> getRecentMemoryPlaceByMarriedId(Long marriedId, MemoryPlaceFindDto findCondition);
}
