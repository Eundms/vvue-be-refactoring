package com.exciting.vvue.notification.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exciting.vvue.notification.NotificationService;
import com.exciting.vvue.notification.exception.NotificationFailException;
import com.exciting.vvue.notification.exception.UserNotAddedToNotifyException;
import com.exciting.vvue.notification.model.EventType;
import com.exciting.vvue.notification.model.NotificationContent;
import com.exciting.vvue.notification.model.NotificationType;
import com.exciting.vvue.notification.model.Subscriber;
import com.exciting.vvue.notification.model.VvueNotification;
import com.exciting.vvue.notification.dto.NotReadNotificationDto;
import com.exciting.vvue.notification.dto.VvueNotificationListDto;
import com.exciting.vvue.notification.dto.VvueNotificationResDto;
import com.google.firebase.database.utilities.Pair;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final VvueNotificationRepository vvueNotificationRepository;
	private final SubscriberRepository subscriberRepository;

	private final FirebaseCloudMessageService firebaseCloudMessageService;

	@Transactional
	@Override
	public void subscribe(Long userId, String firebaseToken) {
		Optional<Subscriber> subscriber = subscriberRepository.findByUserId(userId);
		Subscriber nSubscriber;

		if (subscriber.isEmpty()) {
			nSubscriber = Subscriber.from(userId, firebaseToken);
			subscriberRepository.save(nSubscriber);
		} else {
			subscriberRepository.update(userId, firebaseToken);
		}

	}

	@Override
	public boolean unsubscribe(Long userId, String firebaseToken) {
		Optional<Subscriber> subscriber = subscriberRepository.findByUserIdAndFirebaseToken(
			userId, firebaseToken);
		if (subscriber.isEmpty()) {
			return false;
		}
		subscriberRepository.delete(subscriber.get());
		return true;
	}

	public void sendByToken(List<Long> receiverIds, EventType eventType, String[] titleArgs, String[] bodyArgs)
		throws UserNotAddedToNotifyException, NotificationFailException {

		List<Subscriber> subscribers = subscriberRepository.findByUserIdIn(receiverIds);

		List<CompletableFuture<Pair<Long, String>>> futures = subscribers.stream()
			.map(subscriber -> {
				String title = eventType.getTitle();
				String body = eventType.getBody(bodyArgs);

				return firebaseCloudMessageService
					.sendNotification(subscriber.getFirebaseToken(), title, body)
					.thenApply(response -> new Pair<>(subscriber.getUserId(), response));
			})
			.toList();

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		for (CompletableFuture<Pair<Long, String>> future : futures) {
			try {
				Pair<Long, String> result = future.get();
				Long receiverId = result.getFirst();
				String response = result.getSecond();
				if (response.contains("Error")) {
					// TODO : 메시지 다시 보낼 수 있도록 에러 처리
					throw new NotificationFailException("Notification sending failed");
				}
				vvueNotificationRepository.save(VvueNotification.builder()
						.notificationType(eventType.getType())
						.content(NotificationContent.builder()
							.title(eventType.getTitle(titleArgs))
							.body(eventType.getBody(bodyArgs))
							.image(null)
							.build())
						.isRead(false)
						.receiverId(receiverId)
					.build());
			} catch (Exception e) {
				log.error("Error sending notification: ", e);
				throw new NotificationFailException("Error sending notification");
			}
		}
	}

	@Override
	public VvueNotificationListDto getAllNotificationBy(Long userId, Long nextCursor,
		int size) {
		log.debug("[SIZE]>> " + vvueNotificationRepository.findByReceiverId(userId).size());
		List<VvueNotification> allNotifications =
			vvueNotificationRepository.findByReceiverId(userId);
		List<VvueNotificationResDto> res = allNotifications
			.stream()
			.filter(x -> x.getId() > nextCursor)
			.sorted(Comparator.comparing(VvueNotification::getCreatedAt).reversed())
			.limit(size)
			.map(VvueNotificationResDto::from)
			.toList();
		log.debug("[SIZE2]>> " + res.size());
		Optional<VvueNotificationResDto> maxValue = res.stream()
			.max(Comparator.comparing(VvueNotificationResDto::getId));
		Long lastCursorId = maxValue.isEmpty() ? -1 : maxValue.get().getId();
		long nextNotifyCnt = allNotifications
			.stream()
			.filter(x -> x.getId() > lastCursorId)
			.count();

		log.debug("[SIZE3] " + res.size());
		return VvueNotificationListDto.builder()
			.vvueNotificationResDtoList(res)
			.lastCursorId(lastCursorId)
			.hasNext(nextNotifyCnt != 0)
			.build();
	}

	@Override
	public NotReadNotificationDto getUnReadNotificationBy(Long userId) {
		int cnt = vvueNotificationRepository.countUnReadByReceiverId(userId);
		return new NotReadNotificationDto(cnt);
	}

	@Transactional
	@Override
	public void readAllUnReadNotify(Long userId) {
		vvueNotificationRepository.readAllUnReadNotify(userId);
	}

	@Transactional
	@Override
	public void readUnReadNotify(Long userId, Long notificationId) {
		vvueNotificationRepository.readUnReadNotify(userId, notificationId);

	}

}
