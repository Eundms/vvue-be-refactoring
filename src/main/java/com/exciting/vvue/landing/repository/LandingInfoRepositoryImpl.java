package com.exciting.vvue.landing.repository;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.landing.repository.jpa.LandingInfoJpaRepository;
import com.exciting.vvue.landing.model.dto.LandingInfos;
import com.exciting.vvue.landing.service.LandingRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LandingInfoRepositoryImpl implements LandingRepository {
	private final LandingInfoJpaRepository landingInfoJpaRepository;

	@Override
	public LandingInfos getAllRelatedInfo(Long userId) {
		return landingInfoJpaRepository.findUserRelatedInfo(userId);
	}
}
