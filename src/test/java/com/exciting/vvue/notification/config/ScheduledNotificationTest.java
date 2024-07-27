package com.exciting.vvue.notification.config;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.notification.model.NotificationType;
import com.exciting.vvue.schedule.model.DateType;
import com.exciting.vvue.schedule.model.RepeatCycle;
import com.exciting.vvue.schedule.model.Schedule;
import com.exciting.vvue.user.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduledNotificationTest {
    @Test
    @DisplayName("하루전, 년 반복, 결혼")
    public void reserveMarried(){
        LocalDate now = LocalDate.now();
        LocalDate after = now.plusDays(1);
        Schedule schedule = Schedule.builder()
            .id(1L)
            .married(
                Married.builder()
                    .marriedDay(LocalDate.of(1997,after.getMonth(),after.getDayOfMonth()))
                    .first(User.builder().id(3L).build())
                    .second(User.builder().id(4L).build())
                    .build()
            )
            .scheduleDate(LocalDate.of(2023,8,after.getDayOfMonth()))
            .scheduleName("동일 날짜 (월반복)")
            .repeatCycle(RepeatCycle.MONTHLY)
            .dateType(DateType.NORMAL)
            .build();

        int day = 1;
        String body = "";
        NotificationType notificationType = null;
        List<Long> receiverIds = new ArrayList<>();
        if (schedule.getDateType()==DateType.NORMAL) { // SCHEDULE
            // [부부모두] 일정
            notificationType = NotificationType.SCHEDULE;
            body = "일정(" + schedule.getScheduleName() + ")이 " + day
                + "일 남았어요\n다른 사람들은 이런 곳을 좋아한데요";
            receiverIds.add(schedule.getMarried().getFirst().getId());
            receiverIds.add(schedule.getMarried().getSecond().getId());
        }
        Assertions.assertThat(receiverIds).containsAll(List.of(3L,4L));
    }
    @Test
    @DisplayName("하루전, 월 반복, 일반 일정")
    public void reserveNotificationTest(){
        LocalDate now = LocalDate.now();
        LocalDate after = now.plusDays(1);
        Schedule schedule = Schedule.builder()
            .id(1L)
            .married(
                Married.builder()
                    .first(User.builder().id(3L).build())
                    .second(User.builder().id(4L).build())
                    .build()
            )
            .scheduleDate(LocalDate.of(2023,8,after.getDayOfMonth()))
            .scheduleName("동일 날짜 (월반복)")
            .repeatCycle(RepeatCycle.MONTHLY)
            .dateType(DateType.NORMAL)
            .build();

        int day = 1;
        String body = "";
        NotificationType notificationType = null;
        List<Long> receiverIds = new ArrayList<>();
        if (schedule.getDateType()==DateType.NORMAL) { // SCHEDULE
            // [부부모두] 일정
            notificationType = NotificationType.SCHEDULE;
            body = "일정(" + schedule.getScheduleName() + ")이 " + day
                + "일 남았어요\n다른 사람들은 이런 곳을 좋아한데요";
            receiverIds.add(schedule.getMarried().getFirst().getId());
            receiverIds.add(schedule.getMarried().getSecond().getId());
        }
        Assertions.assertThat(receiverIds).containsAll(List.of(3L,4L));
    }
    @Test
    @DisplayName("하루전, 년 반복, 생일")
    public void reserveBirthday(){
        LocalDate now = LocalDate.now();
        LocalDate after = now.plusDays(1);

        Schedule schedule = Schedule.builder()
            .id(1L)
            .married(
                Married.builder()
                    .first(User.builder().id(3L).birthday(LocalDate.of(1999,8,after.getDayOfMonth())).build())
                    .second(User.builder().id(4L).birthday(LocalDate.of(1992,8,after.getDayOfMonth())).build())
                    .build()
            )
            .scheduleDate(LocalDate.of(1999,8,now.getDayOfMonth()))
            .scheduleName("동일 날짜 (년 반복)")
            .repeatCycle(RepeatCycle.YEARLY)
            .dateType(DateType.FEMALEBIRTHDAY)
            .build();

        int day = 1;
        String body = "";
        NotificationType notificationType = null;
        List<Long> receiverIds = new ArrayList<>();
        if (schedule.getDateType() == DateType.MALEBIRTHDAY || schedule.getDateType() == DateType.FEMALEBIRTHDAY) {
            User user = schedule.getMarried().getFirst();
            if (user.getBirthday() != schedule.getScheduleDate()) {
                user = schedule.getMarried().getSecond();
            }

            notificationType = NotificationType.BIRTH;
            body = "배우자의 생일이 " + day + "일 남았어요\n다른 사람들은 이런 곳을 좋아한데요";
            receiverIds.add(user.getId());
        }
        Assertions.assertThat(receiverIds).containsAll(List.of(4L));
    }

    @Test
    @DisplayName("이틀 전, 년 반복, 생일")
    public void reserve(){
        LocalDate now = LocalDate.now();
        LocalDate after = now.plusDays(2);
        Schedule schedule = Schedule.builder()
            .id(1L)
            .married(
                Married.builder()
                    .first(User.builder().id(3L).birthday(LocalDate.of(1999,8,after.getDayOfMonth())).build())
                    .second(User.builder().id(4L).birthday(LocalDate.of(1992,8,after.getDayOfMonth())).build())
                    .build()
            )
            .scheduleDate(LocalDate.of(1999,8,now.getDayOfMonth()))
            .scheduleName("동일 날짜 (년 반복)")
            .repeatCycle(RepeatCycle.YEARLY)
            .dateType(DateType.FEMALEBIRTHDAY)
            .build();

        int day = 1;
        String body = "";
        NotificationType notificationType = null;
        List<Long> receiverIds = new ArrayList<>();
        if (schedule.getDateType() == DateType.MALEBIRTHDAY || schedule.getDateType() == DateType.FEMALEBIRTHDAY) {
            User user = schedule.getMarried().getFirst();
            if (user.getBirthday() != schedule.getScheduleDate()) {
                user = schedule.getMarried().getSecond();
            }
            notificationType = NotificationType.BIRTH;
            body = "배우자의 생일이 " + day + "일 남았어요\n다른 사람들은 이런 곳을 좋아한데요";
            receiverIds.add(user.getId());
        }
        Assertions.assertThat(receiverIds).containsAll(List.of(4L));
    }
}