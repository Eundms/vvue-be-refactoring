package com.exciting.vvue.landing.service;

import org.springframework.stereotype.Service;

import com.exciting.vvue.landing.model.dto.LandingInfos;
import com.exciting.vvue.landing.LandingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LandingServiceImpl implements LandingService {
	private final LandingRepository landingRepository;
	@Override
	public LandingInfos getAllRelatedInfo(Long userId) {
		return landingRepository.getAllRelatedInfo(userId);
	}
}
