package com.exciting.vvue.landing;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.landing.model.LandingStatus;
import com.exciting.vvue.landing.model.dto.LandingInfos;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/landing")
@RequiredArgsConstructor
public class LandingController {
	private final LandingService landingService;
	private final LandingStateEmitService landingStateEmitService;
	@Operation(summary = "모든 정보(부부연결유무/유저정보-성별,생일,닉네임) 입력 여부 확인")
	@GetMapping("/stage")
	public SseEmitter subscribeLandingState() { // SSE Stream을 구독하는 API
		Long userId = AuthContext.getUserId();

		LandingInfos landingInfos = landingService.getAllRelatedInfo(userId);

		SseEmitter emitter = null;
		LandingStatus stage = LandingStatus.from(landingInfos); // 현재 상태
		if (stage == LandingStatus.COMPLETE || stage == LandingStatus.CODED) {
			emitter = landingStateEmitService.subscribeLandingState("MARRIED", landingInfos.getMarriedId());
		} else {
			emitter = landingStateEmitService.subscribeLandingState("USER", landingInfos.getUserId());
		}

		emitter = sendImmediatly(emitter, stage);

		return emitter;
	}
	private SseEmitter sendImmediatly(SseEmitter emitter, LandingStatus status) {

		try {
			// Send the initial state immediately to the client
			emitter.send(SseEmitter.event().name("message").data(status.toString().toLowerCase()));
		} catch (IOException e) {
			log.error("Error sending initial state", e);
		}
		return emitter;
	}

}
