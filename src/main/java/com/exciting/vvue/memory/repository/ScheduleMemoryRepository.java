package com.exciting.vvue.memory.repository;

import com.exciting.vvue.memory.model.ScheduleMemory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleMemoryRepository extends JpaRepository<ScheduleMemory, Long> {
    @Query("select sm from ScheduleMemory sm where sm.scheduleId=:scheduleId and sm.married.id=:userMarriedId")
    ScheduleMemory findByScheduleIdAndMarriedId(Long scheduleId, Long userMarriedId);

    @Query(value ="select * from schedulememory sm where sm.married_id = :marriedId "
        + "and sm.id > :firstScheduleMemoryId LIMIT :size", nativeQuery = true)
    List<ScheduleMemory> findByMarriedIdWithCursor(Long marriedId, Long firstScheduleMemoryId, int size);

    @Query(value = "select sm from ScheduleMemory sm where sm.married.id=:marriedId")
    List<ScheduleMemory> findAllByMarriedId(Long marriedId);
}
