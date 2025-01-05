package com.exciting.vvue.landing.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.exciting.vvue.landing.LandingStateEmitService;
import com.exciting.vvue.landing.model.LandingStatus;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LandingStateEmitServiceImpl implements LandingStateEmitService {

	// {key: married ID 혹은 user ID,  value: 해당 사용자의 SseEmitter}
	private final ConcurrentHashMap<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

	public SseEmitter subscribeLandingState(String type, Long id) {
		SseEmitter emitter = new SseEmitter();
		String genId = type +"_" + id;
		sseEmitterMap.put(genId, emitter);  // married ID와 SseEmitter를 매핑하여 저장

		// 연결 종료 시, SseEmitter 제거
		emitter.onCompletion(() -> sseEmitterMap.remove(genId));
		emitter.onError((ex) -> sseEmitterMap.remove(genId));

		return emitter;
	}

	public void notifyLandingState(Long id, String type, LandingStatus status) {
		String genId = type + "_" + id;
		SseEmitter emitter = sseEmitterMap.get(genId);
		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event().name("message").data(status.toString().toLowerCase()));
			} catch (Exception e) {
				log.error("Error sending event to user {}: {}", id, e.getMessage());
				emitter.completeWithError(e);
			}
		}
	}


}
