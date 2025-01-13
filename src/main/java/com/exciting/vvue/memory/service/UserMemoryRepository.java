package com.exciting.vvue.memory.service;

import java.time.LocalDate;
import java.util.List;


import com.exciting.vvue.memory.model.UserMemory;

public interface UserMemoryRepository {
	UserMemory findByUserIdAndScheduleMemoryId(Long userId, Long scheduleMemoryId, LocalDate cur);

	void save(UserMemory userMemory);

	List<UserMemory> findByScheduleMemory_Id(Long scheduleMemoryId);
}
