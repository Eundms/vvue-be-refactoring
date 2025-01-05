package com.exciting.vvue.landing.service;

import com.exciting.vvue.landing.model.dto.LandingInfos;

public interface LandingRepository {
	LandingInfos getAllRelatedInfo(Long userId);

}
