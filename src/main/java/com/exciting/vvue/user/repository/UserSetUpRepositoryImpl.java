package com.exciting.vvue.user.repository;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.user.model.dto.UserRelatedInfo;
import com.exciting.vvue.user.repository.jpa.UserSetUpJpaRepository;
import com.exciting.vvue.user.service.UserSetUpRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserSetUpRepositoryImpl implements UserSetUpRepository {
	private final UserSetUpJpaRepository userSetUpJpaRepository;

	@Override
	public UserRelatedInfo getAllRelatedInfo(Long userId) {
		return userSetUpJpaRepository.findUserRelatedInfo(userId);
	}
}
