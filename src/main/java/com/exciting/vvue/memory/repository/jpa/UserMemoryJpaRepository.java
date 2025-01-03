package com.exciting.vvue.memory.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.exciting.vvue.memory.model.UserMemory;

@Repository
public interface UserMemoryJpaRepository extends JpaRepository<UserMemory, Long> {
	@Query("select um from UserMemory um where um.user.id=:userId and um.scheduleMemory.id=:scheduleMemoryId")
	UserMemory findByUserIdAndScheduleMemoryId(Long userId, Long scheduleMemoryId);

}
