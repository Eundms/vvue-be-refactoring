package com.exciting.vvue.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.model.dto.UserRelatedInfo;

public interface UserSetUpJpaRepository extends JpaRepository<User, Long> {

	@Query("SELECT new com.exciting.vvue.user.model.dto.UserRelatedInfo(" +
		"u.isAuthenticated, " +
		"CASE WHEN m IS NOT NULL THEN true ELSE false END, " +
		"CASE WHEN m IS NOT NULL AND m.marriedDay IS NOT NULL THEN true ELSE false END) " +
		"FROM User u " +
		"LEFT JOIN Married m ON m.first.id = u.id OR m.second.id = u.id " +
		"WHERE u.id = :userId")
	UserRelatedInfo findUserRelatedInfo(@Param("userId") Long userId);
}
