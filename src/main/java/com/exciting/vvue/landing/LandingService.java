package com.exciting.vvue.landing;

import com.exciting.vvue.landing.model.dto.LandingInfos;

public interface LandingService {

  LandingInfos getAllRelatedInfo(Long userId);

}
