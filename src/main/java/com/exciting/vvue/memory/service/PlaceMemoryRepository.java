package com.exciting.vvue.memory.service;

import com.exciting.vvue.memory.model.PlaceMemory;
import java.util.List;

public interface PlaceMemoryRepository {

  PlaceMemory save(PlaceMemory placeMemory);

  List<PlaceMemory> saveAll(List<PlaceMemory> placeMemoryList);

  List<PlaceMemory> findByScheduleMemory_Id(Long scheduleMemoryId);

  List<PlaceMemory> findAllByMarriedId(Long marriedId);
}
