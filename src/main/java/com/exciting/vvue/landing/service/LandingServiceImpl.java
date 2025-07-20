package com.exciting.vvue.landing.service;

import com.exciting.vvue.landing.LandingService;
import com.exciting.vvue.landing.model.dto.LandingInfos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LandingServiceImpl implements LandingService {

  private final LandingRepository landingRepository;

  @Override
  public LandingInfos getAllRelatedInfo(Long userId) {
    return landingRepository.getAllRelatedInfo(userId);
  }
}
