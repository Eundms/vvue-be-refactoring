package com.exciting.vvue.notification.config;

import com.exciting.vvue.notification.model.NotificationContent;
import com.exciting.vvue.notification.model.NotificationType;
import com.exciting.vvue.notification.model.dto.NotificationReqDto;
import com.exciting.vvue.notification.service.NotificationService;
import com.exciting.vvue.schedule.model.DateType;
import com.exciting.vvue.schedule.model.Schedule;
import com.exciting.vvue.schedule.model.dto.ScheduleResDto;
import com.exciting.vvue.schedule.service.ScheduleService;
import com.exciting.vvue.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
                String body = "";
                NotificationType notificationType = null;
                List<Long> receiverIds = new ArrayList<>();
                Map<String, String> data = new HashMap<>();
                if (schedule.getDateType() == DateType.WEDDINGANNIVERSARY) {
                    // [부부모두] 알림을 등록한다. NotificationType.MARRIED
                    User user = schedule.getMarried().getFirst();
                    User user2 = schedule.getMarried().getSecond();
                    notificationType = NotificationType.MARRIED;
                    body = "결혼 기념일이 " + day + "일 남았어요\n다른 사람들은 이런 곳을 좋아한데요";
                    receiverIds.add(user.getId());
                    receiverIds.add(user2.getId());

                } else if (schedule.getDateType() == DateType.MALEBIRTHDAY || schedule.getDateType() == DateType.FEMALEBIRTHDAY) {

                    User user = schedule.getMarried().getFirst();
                    if (user.getBirthday() != schedule.getScheduleDate()) {
                        user = schedule.getMarried().getSecond();
                    }

                    notificationType = NotificationType.BIRTH;
                    body = "배우자의 생일이 " + day + "일 남았어요\n다른 사람들은 이런 곳을 좋아한데요";
                    receiverIds.add(user.getId());

                } else { // NORMAL
                    // [부부모두] 일정
                    notificationType = NotificationType.SCHEDULE;
                    body = "일정(" + schedule.getScheduleName() + ")이 " + day
                            + "일 남았어요";
                    receiverIds.add(schedule.getMarried().getFirst().getId());
                    receiverIds.add(schedule.getMarried().getSecond().getId());
                    data.put("scheduleDate", ScheduleResDto.from(schedule).setCommingDate().getCurDate().toString());
                }

                for (Long userId : receiverIds) { // send2AllReceivers
                    notificationService.sendByToken(
                            NotificationReqDto.builder()
                                    .targetUserId(userId)
                                    .type(notificationType)
                                    .content(
                                            NotificationContent
                                                    .builder()
                                                    .title(notificationType.getDescription())
                                                    .body(body)
                                                    .build())
                                    //.data(data)
                                    .build());
                }
            }
        }

    }
}
