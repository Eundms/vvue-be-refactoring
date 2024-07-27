package com.exciting.vvue.memory.repository;

import com.exciting.vvue.memory.model.UserMemory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMemoryRepository extends JpaRepository<UserMemory, Long> {
    @Query("select um from UserMemory um where um.user.id=:userId and um.scheduleMemory.id=:scheduleMemoryId")
    UserMemory findByUserIdAndScheduleMemoryId(Long userId,Long scheduleMemoryId);

}
