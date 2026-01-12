package com.exciting.vvue.schedule;

import static com.exciting.vvue.notification.model.EventType.SCHEDULE_CREATE;
import static com.exciting.vvue.notification.model.EventType.SCHEDULE_UPDATED;

import com.exciting.vvue.common.annotation.AuthenticatedMarried;
import com.exciting.vvue.common.annotation.AuthenticatedUser;
import com.exciting.vvue.common.annotation.RequireMarried;
import com.exciting.vvue.common.dto.MessageResDto;
import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.notification.NotificationService;
import com.exciting.vvue.schedule.dto.req.CalendarSearchReqDto;
import com.exciting.vvue.schedule.dto.req.SchedulePagingReqDto;
import com.exciting.vvue.schedule.dto.req.ScheduleReqDto;
import com.exciting.vvue.schedule.dto.res.CalendarResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleDailyListResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleDailyResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleIdResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleListResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleResDto;
import com.exciting.vvue.schedule.model.Schedule;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@RequireMarried
public class ScheduleController implements ScheduleApi {

  private final ScheduleService scheduleService;
  private final NotificationService notificationService;

  @GetMapping("/{scheduleId}")
  @Override
  public ScheduleResDto get(
      @PathVariable("scheduleId") Long scheduleId,
      @AuthenticatedMarried Married married) {

    ScheduleResDto scheduleResDto = scheduleService.getSchedule(scheduleId);
    if (!scheduleResDto.getMarriedId().equals(married.getId())) {
      throw new VvueException(ErrorCode.UNAUTHORIZED_USER);
    }

    return scheduleResDto;
  }

  @PostMapping
  @Override
  public ScheduleIdResDto add(
      @RequestBody ScheduleReqDto scheduleReqDto,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married) {

    Schedule schedule = scheduleService.addSchedule(married, scheduleReqDto);

    Long peerId = married.getPeerId(userId);
    notificationService.sendByToken(
        List.of(peerId),
        SCHEDULE_CREATE,
        new String[]{schedule.getScheduleName()},
        new String[]{schedule.getScheduleName(), schedule.getScheduleDate().toString()}
    );

    return ScheduleIdResDto.of(schedule.getId());
  }

  @PostMapping("/marry")
  @Override
  public MessageResDto addMarry(@AuthenticatedMarried Married married) {
    scheduleService.addAnniversaryAndBirthday(married.getId());
    return MessageResDto.of("기념일 생일 등록 성공");
  }

  @PutMapping("/{scheduleId}")
  @Override
  public ScheduleIdResDto modify(
      @PathVariable("scheduleId") Long scheduleId,
      @RequestBody ScheduleReqDto scheduleReqDto,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married) {

    Schedule schedule = scheduleService.modifySchedule(married.getId(), scheduleId, scheduleReqDto);

    Long peerId = married.getPeerId(userId);
    notificationService.sendByToken(
        List.of(peerId),
        SCHEDULE_UPDATED,
        new String[]{schedule.getScheduleName()},
        new String[]{schedule.getScheduleName(), schedule.getScheduleDate().toString()}
    );

    return ScheduleIdResDto.of(schedule.getId());
  }

  @DeleteMapping("/{scheduleId}")
  @Override
  public MessageResDto delete(
      @PathVariable("scheduleId") Long scheduleId,
      @AuthenticatedMarried Married married) {

    if (!scheduleService.existsById(scheduleId)) {
      throw new VvueException(ErrorCode.SCHEDULE_NOT_FOUND);
    }

    scheduleService.deleteSchedule(married.getId(), scheduleId);

    notificationService.sendByToken(
        List.of(married.getId()),
        SCHEDULE_UPDATED,
        null,
        null
    );

    return MessageResDto.of("일정 삭제 성공");
  }

  @GetMapping("/calendar")
  @Override
  public CalendarResDto getCalendar(
      @Valid @ModelAttribute CalendarSearchReqDto searchReqDto,
      @AuthenticatedMarried Married married) {

    int year = searchReqDto.getYear();
    int month = searchReqDto.getMonth();
    List<String> scheduledDates = scheduleService.getScheduledDateOnCalendar(married.getId(), year, month);
    return CalendarResDto.of(year, month, scheduledDates);
  }

  @GetMapping("/calendar-daily")
  @Override
  public ScheduleDailyListResDto getDaily(
      @RequestParam("date") LocalDate date,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married) {

    List<ScheduleDailyResDto> schedules = scheduleService.getScheduleOnDate(married.getId(), userId, date);
    return ScheduleDailyListResDto.of(date, schedules);
  }

  @GetMapping("/dday")
  @Override
  public ScheduleListResDto getDday(
      @Valid @ModelAttribute SchedulePagingReqDto pagingReqDto,
      @AuthenticatedMarried Married married) {

    long idCursor = pagingReqDto.getIdCursor() != null ? pagingReqDto.getIdCursor() : -1L;
    int size = pagingReqDto.getSize();
    return scheduleService.getAllSchedule(married, idCursor, size);
  }
}