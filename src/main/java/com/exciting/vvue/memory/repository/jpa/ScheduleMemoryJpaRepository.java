package com.exciting.vvue.memory.repository.jpa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.exciting.vvue.memory.model.ScheduleMemory;

public interface ScheduleMemoryJpaRepository extends JpaRepository<ScheduleMemory, Long> {
	@Query("select sm from ScheduleMemory sm where sm.scheduleId=:scheduleId and sm.married.id=:userMarriedId and sm.scheduleDate=:day")
	ScheduleMemory findByScheduleIdAndMarriedIdAndDate(Long scheduleId, Long userMarriedId, LocalDate day);

	@Query(value = "select * from schedulememory sm where sm.married_id = :marriedId "
		+ "and sm.id > :firstScheduleMemoryId LIMIT :size", nativeQuery = true)
	List<ScheduleMemory> findByMarriedIdWithCursor(Long marriedId, Long firstScheduleMemoryId, int size);

	@Query(value = "select sm from ScheduleMemory sm where sm.married.id=:marriedId")
	List<ScheduleMemory> findAllByMarriedId(Long marriedId);
}
