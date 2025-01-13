package com.exciting.vvue.memory.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	public ScheduleMemory findByScheduleIdAndMarriedIdAndDate(Long scheduleId, Long userMarriedId, LocalDate day) {
		return scheduleMemoryJpaRepository.findByScheduleIdAndMarriedIdAndDate(scheduleId, userMarriedId, day);
	}

	@Override
	public List<ScheduleMemory> findByMarriedIdWithCursor(Long marriedId, Long firstScheduleMemoryId, int size) {
		Pageable pageable = PageRequest.of(0, size);
		return scheduleMemoryJpaRepository.findByMarriedIdWithCursor(marriedId, firstScheduleMemoryId, pageable);
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

	@Override
	public Long countByMarriedIdAndIdGreaterThan(Long marriedId, Long lastCursorId) {
		return scheduleMemoryJpaRepository.countByMarriedIdAndIdGreaterThan(marriedId, lastCursorId);
	}

}
