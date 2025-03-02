package com.exciting.vvue.notification.model;

import java.util.Arrays;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;

public enum EventType {
	SCHEDULE_CREATE(NotificationType.SCHEDULE, "CREATE", "PEER",
		"일정 생성 : %s", "새로운 일정(%s)이 추가되었어요."),
	SCHEDULE_UPDATED(NotificationType.SCHEDULE, "UPDATE", "PEER",
		"일정 변경 : %s", "일정(%s %s)이 변경되었어요."),
	SCHEDULE_DELETED(NotificationType.SCHEDULE, "DELETE", "PEER",
		"일정 삭제", "일정이 삭제되었어요"),
	SCHEDULE_REMIND(NotificationType.SCHEDULE, "REMIND", "BOTH",
		"일정 알림!", "일정(%s)이 %일 남았어요"),
	BIRTH_REMIND(NotificationType.BIRTH, "REMIND", "PEER",
		"일정 알림!" , "배우자의 생일이 %s일 남았어요\n다른 사람들은 이런 곳을 좋아한데요"),
	WEDDING_REMIND(NotificationType.BIRTH, "REMIND", "BOTH",
		"일정 알림!" , "결혼 기념일이 %s일 남았어요\n다른 사람들은 이런 곳을 좋아한데요"),
	;

	@Getter
	private final NotificationType type;
	private final String detail;
	private final String messageTo;
	private final String title;
	private final String body;

	EventType(NotificationType type, String detail, String messageTo, String title, String body) {
		this.type = type;
		this.detail = detail;
		this.messageTo = messageTo;
		this.title = title;
		this.body = body;
	}

	public static Optional<EventType> create(NotificationType notificationType, String detail) {
		return Arrays.stream(EventType.values())
			.filter(x -> x.type == notificationType && x.detail.equals(detail))
			.findFirst();
	}

	public String getTitle(String... titleArgs) {
		return String.format(title, (Object[]) titleArgs);
	}

	public String getBody(String[] bodyArgs) {
		return String.format(body,  (Object[]) bodyArgs);
	}

}
