package com.exciting.vvue.memory.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.exciting.vvue.memory.model.ScheduleMemory;

public interface ScheduleMemoryRepository {

	ScheduleMemory findByScheduleIdAndMarriedIdAndDate(Long scheduleId, Long userMarriedId, LocalDate day);

	List<ScheduleMemory> findByMarriedIdWithCursor(Long marriedId, Long firstScheduleMemoryId, int size);

	List<ScheduleMemory> findAllByMarriedId(Long marriedId);

	ScheduleMemory save(ScheduleMemory scheduleMemory);

	Optional<ScheduleMemory> findById(Long scheduleMemoryId);

	void deleteById(Long memoryId);

	Long countByMarriedIdAndIdGreaterThan(Long id, Long lastCursorId);
}
