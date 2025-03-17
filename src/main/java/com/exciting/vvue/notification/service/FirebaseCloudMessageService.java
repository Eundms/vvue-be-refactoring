package com.exciting.vvue.notification.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirebaseCloudMessageService {
	@Async
	public CompletableFuture<String> sendNotification(String token, String title, String body) {
		try {
			Message firebaseMessage = Message.builder()
				.setToken(token)
				.setNotification(Notification.builder()
					.setTitle(title)
					.setBody(body)
					.build())
				.build();

			String response = FirebaseMessaging.getInstance().sendAsync(firebaseMessage).get();
			return CompletableFuture.completedFuture(response);
		} catch (Exception e) {
			return CompletableFuture.completedFuture("Error: " + e.getMessage());
		}
	}
}
