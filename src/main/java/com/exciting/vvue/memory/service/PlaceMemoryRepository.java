package com.exciting.vvue.memory.service;

import java.util.List;

import com.exciting.vvue.memory.model.PlaceMemory;

public interface PlaceMemoryRepository {
	PlaceMemory save(PlaceMemory placeMemory);
	List<PlaceMemory> saveAll(List<PlaceMemory> placeMemoryList);
}
