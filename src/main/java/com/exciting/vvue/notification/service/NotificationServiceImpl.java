package com.exciting.vvue.notification.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exciting.vvue.notification.NotificationService;
import com.exciting.vvue.notification.exception.NotificationFailException;
import com.exciting.vvue.notification.exception.UserNotAddedToNotifyException;
import com.exciting.vvue.notification.model.Subscriber;
import com.exciting.vvue.notification.model.VvueNotification;
import com.exciting.vvue.notification.dto.NotReadNotificationDto;
import com.exciting.vvue.notification.dto.NotificationReqDto;
import com.exciting.vvue.notification.dto.VvueNotificationListDto;
import com.exciting.vvue.notification.dto.VvueNotificationResDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final VvueNotificationRepository vvueNotificationRepository;
	private final SubscriberRepository subscriberRepository;

	//    private final FirebaseMessaging firebaseMessaging;
	//    private final FirebaseCloudMessageService firebaseCloudMessageService;
	@Override
	public void subscribe(Long userId, String firebaseToken) {
		subscriberRepository.save(Subscriber.from(userId, firebaseToken));
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

	@Override
	@Transactional
	public void sendByToken(NotificationReqDto notificationReqDto)
		throws UserNotAddedToNotifyException, NotificationFailException {
		log.debug("{sendByToken} " + notificationReqDto);
		Optional<Subscriber> notificationUser = subscriberRepository.findByUserId(
			notificationReqDto.getTargetUserId());
		log.debug("SENDBYTOKEN1{}" + notificationUser.isEmpty());
		if (!notificationUser.isPresent()) {
			return;
		}
		log.debug("SENDBYTOKEN2{}" + notificationUser.isEmpty());
		log.debug("[NOTIFY] 전");

		VvueNotification vvueNotification = VvueNotification.builder()
			.notificationType(notificationReqDto.getType())
			.content(notificationReqDto.getContent())
			.isRead(false)
			.receiverId(notificationReqDto.getTargetUserId())
			//.data(jsonString)
			.build();
		vvueNotificationRepository.save(vvueNotification);
		log.debug("[NOTIFY]" + vvueNotification);
		// title, body, image
		//        firebaseCloudMessageService.sendMessageTo(notificationUser.get().getFirebaseToken(),
		//            notificationReqDto.getContent().getTitle(),
		//            notificationReqDto.getContent().getBody());
		//         Message message = Message.builder()
		//                 .putData("title", notificationReqDto.getContent().getTitle())
		//                 .putData("body", notificationReqDto.getContent().getBody())
		//                 .setToken(notificationUser.get().getFirebaseToken())
		//                 .build();
		//         FirebaseMessaging.getInstance().sendAsync(message);
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
			.hasNext(nextNotifyCnt == 0 ? false : true)
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
