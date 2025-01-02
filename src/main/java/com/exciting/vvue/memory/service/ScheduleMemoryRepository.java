package com.exciting.vvue.memory.service;

import java.util.List;
import java.util.Optional;

import com.exciting.vvue.memory.model.ScheduleMemory;

public interface ScheduleMemoryRepository {

	ScheduleMemory findByScheduleIdAndMarriedId(Long scheduleId, Long userMarriedId);

	List<ScheduleMemory> findByMarriedIdWithCursor(Long marriedId, Long firstScheduleMemoryId, int size);

	List<ScheduleMemory> findAllByMarriedId(Long marriedId);

	ScheduleMemory save(ScheduleMemory scheduleMemory);

	Optional<ScheduleMemory> findById(Long scheduleMemoryId);

	void deleteById(Long memoryId);
}
