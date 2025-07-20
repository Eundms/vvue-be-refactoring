package com.exciting.vvue.landing.model.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class LandingInfos {

  private Long userId;
  private boolean isAuthenticated;
  private Long marriedId;
  private LocalDate marriedDay;

  public LandingInfos(Long userId, boolean isAuthenticated, Long marriedId, LocalDate marriedDay) {
    this.userId = userId;
    this.isAuthenticated = isAuthenticated;
    this.marriedId = marriedId;
    this.marriedDay = marriedDay;
  }
}
