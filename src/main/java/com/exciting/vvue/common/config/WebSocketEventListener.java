package com.exciting.vvue.common.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.exciting.vvue.landing.LandingService;
import com.exciting.vvue.landing.model.LandingStatus;
import com.exciting.vvue.landing.model.dto.LandingInfos;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor

public class WebSocketEventListener {
	private final SimpMessagingTemplate messagingTemplate;
	private final LandingService landingService;

	@EventListener
	public void handleSubscriptionEvent(SessionSubscribeEvent event) {
		String destination = event.getMessage().getHeaders().get("simpDestination").toString();

		// 특정 토픽 구독 시 초기 데이터 전송
		if (destination.startsWith("/topic/user/")) {
			String userId = destination.split("/")[3]; // userId 추출
			LandingInfos landingInfos = landingService.getAllRelatedInfo(Long.parseLong(userId));
			LandingStatus stage = LandingStatus.from(landingInfos);
			String state = stage.toString().toLowerCase();
			messagingTemplate.convertAndSend("/topic/user/" + userId + "/married-status", state);

		}
	}
}
