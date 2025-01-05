package com.exciting.vvue.landing;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.exciting.vvue.landing.model.LandingStatus;

public interface LandingStateEmitService {
	SseEmitter subscribeLandingState(String type, Long id);

	void notifyLandingState(Long id, String type, LandingStatus status);
}
