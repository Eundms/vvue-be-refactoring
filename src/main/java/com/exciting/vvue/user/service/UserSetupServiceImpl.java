package com.exciting.vvue.user.service;

import org.springframework.stereotype.Service;

import com.exciting.vvue.user.UserSetupService;
import com.exciting.vvue.user.model.dto.UserRelatedInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSetupServiceImpl implements UserSetupService {
	private final UserSetUpRepository userSetUpRepository;
	@Override
	public UserRelatedInfo getAllRelatedInfo(Long userId) {
		return userSetUpRepository.getAllRelatedInfo(userId);
	}
}
