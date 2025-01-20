package com.exciting.vvue.schedule.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.exciting.vvue.schedule.model.Schedule;

public interface ScheduleRepository {
	Optional<Schedule> findByIdAndMarriedId(Long id, Long marriedId);

	// 앞으로의 일정 쿼리
	List<Schedule> findByMarriedAndFuture(Long marriedId, int typeCursor, LocalDate dateCursor, long idCursor,
		int size);

	List<Integer> findByMarried_IdAndYearAndMonth(Long marriedId, int year, int month);

	List<Schedule> findByMarried_IdAndScheduleDate(Long marriedId, LocalDate date);

	List<Schedule> findByScheduleDate(LocalDate date);

	Long getScheduleMemoryIdByIdAndScheduleDate(Long scheduleId, LocalDate scheduleDate);

	Optional<Schedule> findById(Long scheduleId);

	Schedule save(Schedule from);

	void delete(Schedule schedule);

	boolean existsById(Long scheduleId);

	void saveAll(List<Schedule> schedules);
}
