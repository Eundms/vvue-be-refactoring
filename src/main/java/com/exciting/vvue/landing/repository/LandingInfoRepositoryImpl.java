package com.exciting.vvue.landing.repository;

import com.exciting.vvue.landing.model.dto.LandingInfos;
import com.exciting.vvue.landing.repository.jpa.LandingInfoJpaRepository;
import com.exciting.vvue.landing.service.LandingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LandingInfoRepositoryImpl implements LandingRepository {

  private final LandingInfoJpaRepository landingInfoJpaRepository;

  @Override
  public LandingInfos getAllRelatedInfo(Long userId) {
    return landingInfoJpaRepository.findUserRelatedInfo(userId);
  }
}
