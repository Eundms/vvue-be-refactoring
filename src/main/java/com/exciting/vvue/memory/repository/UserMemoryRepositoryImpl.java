package com.exciting.vvue.memory.repository;

import com.exciting.vvue.memory.model.UserMemory;
import com.exciting.vvue.memory.repository.jpa.UserMemoryJpaRepository;
import com.exciting.vvue.memory.service.UserMemoryRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserMemoryRepositoryImpl implements UserMemoryRepository {

  private final UserMemoryJpaRepository userMemoryJpaRepository;

  @Override
  public UserMemory findByUserIdAndScheduleMemoryId(Long userId, Long scheduleMemoryId,
      LocalDate cur) {
    return userMemoryJpaRepository.findByUserIdAndScheduleMemoryId(userId, scheduleMemoryId, cur);
  }

  @Override
  public void save(UserMemory userMemory) {
    userMemoryJpaRepository.save(userMemory);
  }

  @Override
  public List<UserMemory> findByScheduleMemory_Id(Long scheduleMemoryId) {
    return userMemoryJpaRepository.findByScheduleMemory_Id(scheduleMemoryId);
  }
}
