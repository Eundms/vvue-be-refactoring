package com.exciting.vvue.landing;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.landing.model.LandingStatus;
import com.exciting.vvue.landing.model.dto.LandingInfos;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/landing")
@RequiredArgsConstructor
public class LandingController {

  private final LandingService landingService;

  @Operation(summary = "모든 정보(부부연결유무/유저정보-성별,생일,닉네임) 입력 여부 확인")
  @GetMapping("/stage")
  public ResponseEntity<String> getLandingState() { // API로 상태를 반환
    Long userId = AuthContext.getUserId();

    LandingInfos landingInfos = landingService.getAllRelatedInfo(userId);
    LandingStatus stage = LandingStatus.from(landingInfos);
    String ss = stage.toString().toLowerCase();

    return ResponseEntity.ok(ss);
  }
}
