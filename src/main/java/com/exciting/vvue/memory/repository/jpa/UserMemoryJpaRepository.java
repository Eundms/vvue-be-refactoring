package com.exciting.vvue.memory.repository.jpa;

import com.exciting.vvue.memory.model.UserMemory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMemoryJpaRepository extends JpaRepository<UserMemory, Long> {

  @Query("select um from UserMemory um where um.user.id=:userId and um.scheduleMemory.id=:scheduleMemoryId and um.scheduleMemory.scheduleDate = :cur")
  UserMemory findByUserIdAndScheduleMemoryId(Long userId, Long scheduleMemoryId, LocalDate cur);

  @EntityGraph(value = "UserMemory.withDetails", type = EntityGraph.EntityGraphType.LOAD)
  List<UserMemory> findByScheduleMemory_Id(Long scheduleMemoryId);
}
