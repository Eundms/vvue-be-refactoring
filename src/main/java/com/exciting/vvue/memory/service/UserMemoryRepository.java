package com.exciting.vvue.memory.service;


import com.exciting.vvue.memory.model.UserMemory;

public interface UserMemoryRepository {
    UserMemory findByUserIdAndScheduleMemoryId(Long userId,Long scheduleMemoryId);

    void save(UserMemory userMemory);
}
