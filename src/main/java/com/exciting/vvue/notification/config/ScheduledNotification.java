package com.exciting.vvue.notification.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.exciting.vvue.notification.NotificationService;
import com.exciting.vvue.notification.model.EventType;
import com.exciting.vvue.notification.model.NotificationContent;
import com.exciting.vvue.notification.model.NotificationType;
import com.exciting.vvue.schedule.ScheduleService;
import com.exciting.vvue.schedule.model.DateType;
import com.exciting.vvue.schedule.model.Schedule;
import com.exciting.vvue.schedule.dto.res.ScheduleResDto;
import com.exciting.vvue.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledNotification {

	private final ScheduleService scheduleService;
	private final NotificationService notificationService;

	@Scheduled(cron = "0 0 4 * * *")
	public void reserveDayBeforeSchedule() { //날짜 기반
		// 모든 user에 대해서 스케줄을 확인한다
		for (int day : List.of(7, 1)) {
			List<Schedule> schedules = scheduleService.getAllAfterNDaySchedule(day);
			for (Schedule schedule : schedules) {
				EventType eventType;
				List<Long> receiverIds = new ArrayList<>();
				String[] titleArgs = null;
				String[] bodyArgs = null;

				if (schedule.getDateType() == DateType.WEDDINGANNIVERSARY) {
					// [부부모두] 알림을 등록한다. NotificationType.MARRIED
					User user = schedule.getMarried().getFirst();
					User user2 = schedule.getMarried().getSecond();

					eventType = EventType.WEDDING_REMIND;
					receiverIds.add(user.getId());
					receiverIds.add(user2.getId());

				} else if (schedule.getDateType() == DateType.MALEBIRTHDAY
					|| schedule.getDateType() == DateType.FEMALEBIRTHDAY) {

					User user = schedule.getMarried().getFirst();
					if (user.getBirthday() != schedule.getScheduleDate()) {
						user = schedule.getMarried().getSecond();
					}

					eventType = EventType.BIRTH_REMIND;
					receiverIds.add(user.getId());
					bodyArgs = new String[]{""+day};

				} else { // NORMAL
					// [부부모두] 일정
					eventType = EventType.SCHEDULE_REMIND;
					receiverIds.add(schedule.getMarried().getFirst().getId());
					receiverIds.add(schedule.getMarried().getSecond().getId());
					bodyArgs = new String[]{""+day};

				}
				notificationService.sendByToken(receiverIds, eventType, titleArgs, bodyArgs);
			}
		}

	}
}
