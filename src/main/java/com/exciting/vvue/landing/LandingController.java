package com.exciting.vvue.landing;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.landing.model.LandingStatus;
import com.exciting.vvue.landing.model.dto.LandingInfos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/landing")
@RequiredArgsConstructor
public class LandingController implements LandingApi {

  private final LandingService landingService;

  @GetMapping("/stage")
  @Override
  public String getLandingState() {
    Long userId = AuthContext.getUserId();

    LandingInfos landingInfos = landingService.getAllRelatedInfo(userId);
    LandingStatus stage = LandingStatus.from(landingInfos);

    return stage.toString().toLowerCase();
  }
}
