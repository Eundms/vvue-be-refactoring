package com.exciting.vvue.memory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.memory.model.ScheduleMemory;
import com.exciting.vvue.memory.repository.jpa.ScheduleMemoryJpaRepository;
import com.exciting.vvue.memory.service.ScheduleMemoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ScheduleMemoryRepositoryImpl implements ScheduleMemoryRepository {

	private final ScheduleMemoryJpaRepository scheduleMemoryJpaRepository;

	@Override
	public ScheduleMemory findByScheduleIdAndMarriedId(Long scheduleId, Long userMarriedId) {
		return scheduleMemoryJpaRepository.findByScheduleIdAndMarriedId(scheduleId, userMarriedId);
	}

	@Override
	public List<ScheduleMemory> findByMarriedIdWithCursor(Long marriedId, Long firstScheduleMemoryId, int size) {
		return scheduleMemoryJpaRepository.findByMarriedIdWithCursor(marriedId, firstScheduleMemoryId, size);
	}

	@Override
	public List<ScheduleMemory> findAllByMarriedId(Long marriedId) {
		return scheduleMemoryJpaRepository.findAllByMarriedId(marriedId);
	}

	@Override
	public ScheduleMemory save(ScheduleMemory scheduleMemory) {
		return scheduleMemoryJpaRepository.save(scheduleMemory);
	}

	@Override
	public Optional<ScheduleMemory> findById(Long scheduleMemoryId) {
		return scheduleMemoryJpaRepository.findById(scheduleMemoryId);
	}

	@Override
	public void deleteById(Long memoryId) {
		scheduleMemoryJpaRepository.deleteById(memoryId);
	}
}
