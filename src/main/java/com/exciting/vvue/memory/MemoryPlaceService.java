package com.exciting.vvue.memory;

import java.util.List;

import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.memory.dto.MemoryPlaceFindDto;

public interface MemoryPlaceService {
	List<PlaceMemory> getRecentMemoryPlaceByMarriedId(Long marriedId, MemoryPlaceFindDto findCondition);
}
