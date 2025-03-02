package com.exciting.vvue.notification;

import java.util.List;

import com.exciting.vvue.notification.exception.NotificationFailException;
import com.exciting.vvue.notification.exception.UserNotAddedToNotifyException;
import com.exciting.vvue.notification.dto.NotReadNotificationDto;
import com.exciting.vvue.notification.dto.VvueNotificationListDto;
import com.exciting.vvue.notification.model.EventType;
import com.exciting.vvue.notification.model.NotificationType;

public interface NotificationService {
	void subscribe(Long userId, String firebaseToken);

	boolean unsubscribe(Long userId, String firebaseToken);

	void sendByToken(List<Long> receiverIds, EventType eventType, String[] titleArgs, String[] bodyArgs)
		throws UserNotAddedToNotifyException, NotificationFailException;

	VvueNotificationListDto getAllNotificationBy(Long userId, Long nextCursor, int size);

	NotReadNotificationDto getUnReadNotificationBy(Long userId);

	void readAllUnReadNotify(Long userId);

	void readUnReadNotify(Long userId, Long notifyId);
}
