package com.exciting.vvue.memory.repository.jpa;

import com.exciting.vvue.memory.model.ScheduleMemory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleMemoryJpaRepository extends JpaRepository<ScheduleMemory, Long> {

  @Query("select sm from ScheduleMemory sm where sm.scheduleId=:scheduleId and sm.married.id=:userMarriedId and sm.scheduleDate=:day")
  ScheduleMemory findByScheduleIdAndMarriedIdAndDate(Long scheduleId, Long userMarriedId,
      LocalDate day);

  @Query("SELECT sm FROM ScheduleMemory sm " +
      "WHERE sm.married.id = :marriedId " +
      "AND sm.id > :firstScheduleMemoryId " +
      "ORDER BY sm.id ASC")
  List<ScheduleMemory> findByMarriedIdWithCursor(Long marriedId, Long firstScheduleMemoryId,
      Pageable pageable);

  @Query(value = "select sm from ScheduleMemory sm where sm.married.id=:marriedId")
  List<ScheduleMemory> findAllByMarriedId(Long marriedId);

  @Query("SELECT COUNT(sm) FROM ScheduleMemory sm WHERE sm.married.id = :marriedId AND sm.id > :id")
  Long countByMarriedIdAndIdGreaterThan(@Param("marriedId") Long marriedId,
      @Param("id") Long lastCursorId);
}
