package com.exciting.vvue.landing.model;

import com.exciting.vvue.landing.model.dto.LandingInfos;

public enum LandingStatus {
  COMPLETE,
  CODED,
  AUTHED,
  LOGGED,
  INIT;

  public static LandingStatus from(LandingInfos data) {
    if (data.isAuthenticated() && data.getMarriedId() != null && data.getMarriedDay() != null) {
      return LandingStatus.COMPLETE;
    } else if (data.isAuthenticated() && data.getMarriedId() != null
        && data.getMarriedDay() == null) {
      return LandingStatus.CODED;
    } else if (data.isAuthenticated() && data.getMarriedId() == null
        && data.getMarriedDay() == null) {
      return LandingStatus.AUTHED;
    } else if (!data.isAuthenticated() && data.getMarriedId() == null
        && data.getMarriedDay() == null) {
      return LandingStatus.LOGGED;
    } else {
      return LandingStatus.INIT;
    }
  }
}
