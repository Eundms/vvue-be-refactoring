package com.exciting.vvue.schedule.service;

import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.schedule.dto.req.ScheduleReqDto;
import com.exciting.vvue.schedule.dto.res.ScheduleResDto;
import com.exciting.vvue.schedule.model.RepeatCycle;
import com.exciting.vvue.schedule.model.Schedule;
import com.exciting.vvue.schedule.repository.ScheduleRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScheduleService 테스트")
class ScheduleServiceTest {

    @Mock
    private ScheduleRepositoryImpl scheduleRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private Married married;
    private ScheduleReqDto scheduleReqDto;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        married = Married.builder()
                .id(1L)
                .build();

        scheduleReqDto = new ScheduleReqDto(
                LocalDate.now().plusDays(1),
                "테스트 일정",
                RepeatCycle.NONREPEAT
        );

        schedule = Schedule.builder()
                .id(1L)
                .scheduleName("테스트 일정")
                .scheduleDate(LocalDate.now().plusDays(1))
                .repeatCycle(RepeatCycle.NONREPEAT)
                .married(married)
                .build();
    }

    @Test
    @DisplayName("일정 생성이 성공적으로 수행된다")
    void addScheduleSuccess() {
        // given
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        // when
        Schedule result = scheduleService.addSchedule(married, scheduleReqDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getScheduleName()).isEqualTo("테스트 일정");
        assertThat(result.getMarried().getId()).isEqualTo(1L);
        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    @DisplayName("존재하지 않는 일정 조회 시 예외가 발생한다")
    void getScheduleNotFound() {
        // given
        Long scheduleId = 999L;
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> scheduleService.getSchedule(scheduleId))
                .isInstanceOf(VvueException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SCHEDULE_NOT_FOUND);
    }

    @Test
    @DisplayName("일정 조회가 성공적으로 수행된다")
    void getScheduleSuccess() {
        // given
        Long scheduleId = 1L;
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        // when
        ScheduleResDto result = scheduleService.getSchedule(scheduleId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getScheduleName()).isEqualTo("테스트 일정");
        assertThat(result.getMarriedId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 월의 일정이 있는 날짜 목록을 조회한다")
    void getScheduledDateOnCalendar() {
        // given
        Long marriedId = 1L;
        int year = 2024;
        int month = 1;
        List<Integer> expectedDays = Arrays.asList(15, 20, 25);

        when(scheduleRepository.findByMarried_IdAndYearAndMonth(marriedId, year, month))
                .thenReturn(expectedDays);

        // when
        List<String> result = scheduleService.getScheduledDateOnCalendar(marriedId, year, month);

        // then
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrder("2024-01-15", "2024-01-20", "2024-01-25");
    }

    @Test
    @DisplayName("일정 삭제가 성공적으로 수행된다")
    void deleteScheduleSuccess() {
        // given
        Long marriedId = 1L;
        Long scheduleId = 1L;
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        // when
        scheduleService.deleteSchedule(marriedId, scheduleId);

        // then
        verify(scheduleRepository).delete(schedule);
    }

    @Test
    @DisplayName("다른 부부의 일정 삭제 시 예외가 발생한다")
    void deleteScheduleUnauthorized() {
        // given
        Long wrongMarriedId = 999L;
        Long scheduleId = 1L;
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        // when & then
        assertThatThrownBy(() -> scheduleService.deleteSchedule(wrongMarriedId, scheduleId))
                .isInstanceOf(VvueException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_ACCESS);
    }
}