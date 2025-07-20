package com.exciting.vvue.landing.repository.jpa;

import com.exciting.vvue.landing.model.dto.LandingInfos;
import com.exciting.vvue.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LandingInfoJpaRepository extends JpaRepository<User, Long> {

  @Query("SELECT new com.exciting.vvue.landing.model.dto.LandingInfos(" +
      "u.id, " +
      "CASE " +
      "   WHEN u.gender IS NOT NULL AND u.birthday IS NOT NULL AND u.nickname IS NOT NULL THEN true "
      +
      "   ELSE false " +
      "END, " +
      "COALESCE(m.id, null), " +
      "COALESCE(m.marriedDay, null)) " +
      "FROM User u " +
      "LEFT JOIN Married m ON m.first.id = u.id OR m.second.id = u.id " +
      "WHERE u.id = :userId")
  LandingInfos findUserRelatedInfo(@Param("userId") Long userId);
}
