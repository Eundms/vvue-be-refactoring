package com.exciting.vvue.landing.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.exciting.vvue.user.model.User;
import com.exciting.vvue.landing.model.dto.LandingInfos;

public interface LandingInfoJpaRepository extends JpaRepository<User, Long> {

	@Query("SELECT new com.exciting.vvue.landing.model.dto.LandingInfos(" +
		"u.id, " +
		"u.isAuthenticated, " +
		"COALESCE(m.id, null), " +
		"COALESCE(m.marriedDay, null)) " +
		"FROM User u " +
		"LEFT JOIN Married m ON m.first.id = u.id OR m.second.id = u.id " +
		"WHERE u.id = :userId")
	LandingInfos findUserRelatedInfo(@Param("userId") Long userId);
}
