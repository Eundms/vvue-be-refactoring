package com.exciting.vvue.notification;

import com.exciting.vvue.notification.exception.NotificationFailException;
import com.exciting.vvue.notification.exception.UserNotAddedToNotifyException;
import com.exciting.vvue.notification.dto.NotReadNotificationDto;
import com.exciting.vvue.notification.dto.NotificationReqDto;
import com.exciting.vvue.notification.dto.VvueNotificationListDto;

public interface NotificationService {
	void subscribe(Long userId, String firebaseToken);

	boolean unsubscribe(Long userId, String firebaseToken);

	void sendByToken(NotificationReqDto notificationReqDto)
		throws UserNotAddedToNotifyException, NotificationFailException;

	VvueNotificationListDto getAllNotificationBy(Long userId, Long nextCursor, int size);

	NotReadNotificationDto getUnReadNotificationBy(Long userId);

	void readAllUnReadNotify(Long userId);

	void readUnReadNotify(Long userId, Long notifyId);
}
