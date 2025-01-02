package com.exciting.vvue.memory.repository;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.memory.service.UserMemoryRepository;
import com.exciting.vvue.memory.model.UserMemory;
import com.exciting.vvue.memory.repository.jpa.UserMemoryJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserMemoryRepositoryImpl implements UserMemoryRepository {
	private final UserMemoryJpaRepository userMemoryJpaRepository;

	@Override
	public UserMemory findByUserIdAndScheduleMemoryId(Long userId, Long scheduleMemoryId) {
		return userMemoryJpaRepository.findByUserIdAndScheduleMemoryId(userId, scheduleMemoryId);
	}

	@Override
	public void save(UserMemory userMemory) {
		userMemoryJpaRepository.save(userMemory);
	}
}
