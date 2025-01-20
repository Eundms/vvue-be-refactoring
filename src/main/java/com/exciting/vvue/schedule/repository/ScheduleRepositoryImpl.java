package com.exciting.vvue.schedule.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.schedule.model.Schedule;
import com.exciting.vvue.schedule.repository.jpa.ScheduleJpaRepository;
import com.exciting.vvue.schedule.service.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
	private final ScheduleJpaRepository scheduleJpaRepository;

	@Override
	public Optional<Schedule> findByIdAndMarriedId(Long id, Long marriedId) {
		return scheduleJpaRepository.findByIdAndMarriedId(id, marriedId);
	}

	@Override
	public List<Schedule> findByMarriedAndFuture(Long marriedId, int typeCursor, LocalDate dateCursor, long idCursor,
		int resultSize) {
		return scheduleJpaRepository.findByMarriedAndFuture(marriedId, typeCursor, dateCursor, idCursor, resultSize);
	}

	@Override
	public List<Integer> findByMarried_IdAndYearAndMonth(Long marriedId, int year, int month) {
		return scheduleJpaRepository.findByMarried_IdAndYearAndMonth(marriedId, year, month);
	}

	@Override
	public List<Schedule> findByMarried_IdAndScheduleDate(Long marriedId, LocalDate date) {
		return scheduleJpaRepository.findByMarried_IdAndScheduleDate(marriedId, date);
	}

	@Override
	public List<Schedule> findByScheduleDate(LocalDate date) {
		return scheduleJpaRepository.findByScheduleDate(date);
	}

	@Override
	public Long getScheduleMemoryIdByIdAndScheduleDate(Long scheduleId, LocalDate scheduleDate) {
		return scheduleJpaRepository.getScheduleMemoryIdByIdAndScheduleDate(scheduleId, scheduleDate);
	}

	@Override
	public Optional<Schedule> findById(Long scheduleId) {
		return scheduleJpaRepository.findById(scheduleId);
	}

	@Override
	public Schedule save(Schedule from) {
		return scheduleJpaRepository.save(from);
	}

	@Override
	public void delete(Schedule schedule) {
		scheduleJpaRepository.delete(schedule);
	}

	@Override
	public boolean existsById(Long scheduleId) {
		return scheduleJpaRepository.existsById(scheduleId);
	}

	@Override
	public void saveAll(List<Schedule> schedules) {
		scheduleJpaRepository.saveAll(schedules);
	}
}
