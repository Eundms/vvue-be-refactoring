package com.exciting.vvue.schedule.service;

import com.exciting.vvue.auth.exception.UserUnAuthorizedException;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.repository.MarriedRepository;
import com.exciting.vvue.memory.repository.UserMemoryRepository;
import com.exciting.vvue.schedule.exception.ScheduleNotFoundException;
import com.exciting.vvue.schedule.model.DateType;
import com.exciting.vvue.schedule.model.RepeatCycle;
import com.exciting.vvue.schedule.model.Schedule;
import com.exciting.vvue.schedule.model.dto.ScheduleDailyResDto;
import com.exciting.vvue.schedule.model.dto.ScheduleListResDto;
import com.exciting.vvue.schedule.model.dto.ScheduleResDto;
import com.exciting.vvue.schedule.model.dto.ScheduleReqDto;
import com.exciting.vvue.schedule.repository.ScheduleRepository;
import com.exciting.vvue.user.exception.UserNotFoundException;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.repository.UserRepository;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final MarriedRepository marriedRepository;
    private final UserRepository userRepository;
    private final UserMemoryRepository userMemoryRepository;
    @Override
    public List<Schedule> getAllAfterNDaySchedule(int dayAfter){
        LocalDate dayAfterNDays = LocalDate.now().plusDays(dayAfter);
        List<Schedule> scheduleList = scheduleRepository.findByScheduleDate(dayAfterNDays);
        return scheduleList;
    }
    // 일정 조회
    @Override
    public ScheduleResDto getSchedule(Long scheduleId) {
        // 없는 일정을 조회하려고 하는 경우 404
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ScheduleNotFoundException(""+scheduleId)
        );
        ScheduleResDto scheduleResDto = ScheduleResDto.from(schedule);
        scheduleResDto.setCommingDate();   // curDate에 가장 최근 날짜를 보내줘야 한다.
        return scheduleResDto;
    }

    // 일정 등록
    @Override
    public Schedule addSchedule(Long marriedId, ScheduleReqDto scheduleReqDto) {
        // getById 사용 시 lazy 처리 되기 때문에 더 효율적
        Married married = marriedRepository.getReferenceById(marriedId);
        Schedule schedule = scheduleRepository.save(Schedule.from(scheduleReqDto, married));
        return schedule;
    }

    // 결혼기념일과 생일을 등록하는 메서드
    @Override
    public void addAnniversaryAndBirthday(long marriedId) {
        Married married = marriedRepository.findById(marriedId).orElseThrow(
                () -> new ScheduleNotFoundException("" + marriedId)
        );
        User male = userRepository.findById(married.getFirst().getId()).orElseThrow(
                () -> new UserNotFoundException("" + married.getFirst().getId())
        );
        User female = userRepository.findById(married.getSecond().getId()).orElseThrow(
                () -> new UserNotFoundException("" + married.getSecond().getId())
        );

        Schedule wedding = Schedule.marryAll(married, married.getMarriedDay(), DateType.WEDDINGANNIVERSARY);
        scheduleRepository.save(wedding);
        Schedule maleBirthDay = Schedule.marryAll(married, male.getBirthday(), DateType.MALEBIRTHDAY);
        scheduleRepository.save(maleBirthDay);
        Schedule femaleBirthDay = Schedule.marryAll(married, female.getBirthday(), DateType.FEMALEBIRTHDAY);
        scheduleRepository.save(femaleBirthDay);
    }

    // 일정 등록
    @Override
    public Schedule modifySchedule(Long marriedId, Long scheduleId, ScheduleReqDto scheduleReqDto) {
        // 없는 일정을 수정하려고 하는 경우 404
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                ()-> new ScheduleNotFoundException(""+scheduleId)
        );
        if(schedule.getMarried().getId() != marriedId) throw new UserUnAuthorizedException("[유저]는 해당 [일정]을 수정할 권한이 없습니다.");
        // 받아온 schedule의 일정명, 날짜, 반복 여부를 바꿔준다.
        schedule.updateSchedule(scheduleReqDto);
        return scheduleRepository.save(schedule);
    }

    // 일정 삭제
    @Override
    public void deleteSchedule(Long marriedId, Long scheduleId) {
        // 없는 일정을 삭제하려고 하는 경우 404
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ScheduleNotFoundException("" + scheduleId)
        );
        if(schedule.getMarried().getId() != marriedId){
            throw new UserUnAuthorizedException("[유저]는 해당 [일정]을 삭제할 권한이 없습니다.");
        }
        scheduleRepository.delete(schedule);
    }

    // D-day 모든 스케쥴 가져오기
    @Override
    public ScheduleListResDto getAllSchedule(Long marriedId, long idCursor, int size) {
//        int isNormal = typeCursor == DateType.NORMAL? 1:0;
        Married married = marriedRepository.getReferenceById(marriedId);
        ScheduleResDto cursor = ScheduleResDto.from(scheduleRepository.findById(idCursor).orElse(Schedule.builder().id(Long.valueOf(0)).married(married).scheduleDate(LocalDate.now()).dateType(DateType.WEDDINGANNIVERSARY).repeatCycle(RepeatCycle.NONREPEAT).scheduleName("").build()));
        cursor.setCommingDate();
        //MONTHLY나 YEARLY의 경우 가장 가까운 일정을 나타내야함.
        List<ScheduleResDto> scheduleResDtoList = scheduleRepository.findByMarriedAndFuture(marriedId, cursor.getDateType() == DateType.NORMAL?1:0, LocalDate.parse(cursor.getCurDate(),DateTimeFormatter.ofPattern("yyyy-MM-dd")), cursor.getId(), size + 1).stream()
                .map(ScheduleResDto::from)
                .map(scheduleDto -> scheduleDto.setCommingDate())
                // 일정과 날짜가 다르면 필터링 해준다.
                // setCommingDate에 쓰는 LocalDate add가 그 달의 마지막 날을 넘어가면 마지막 날로 변환해주기 때문.
                // (DB에서 where문으로 필터링 한번 해주지만 확실하게)
                .filter(scheduleDto -> LocalDate.parse(scheduleDto.getScheduleDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).getDayOfMonth()
                    == LocalDate.parse(scheduleDto.getCurDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).getDayOfMonth())
                .collect(Collectors.toList());

        boolean hasNext = true;
        if(scheduleResDtoList.size() <= size){
            hasNext = false;
        }else{
            if(scheduleResDtoList.size() > 0)
                scheduleResDtoList.remove(scheduleResDtoList.size()-1);
        }
        Long lastId = -1L;
        if(scheduleResDtoList.size() > 0)
            lastId = scheduleResDtoList.get(scheduleResDtoList.size() - 1).getId();

        ScheduleListResDto scheduleListResDto = ScheduleListResDto.builder().scheduleResDtoList(scheduleResDtoList).hasNext(hasNext).lastId(lastId).build();

        return scheduleListResDto;
    }

    // 무한 스크롤에서 다음 row가 있는지 확인
    @Override
    public boolean hasNext(List<ScheduleResDto> scheduleResDtoList, int size) {
        if(scheduleResDtoList.size() > size){
            scheduleResDtoList.remove(size);
            return true;
        }
        return false;
    }

    // year년 month월의 모든 스케쥴을 가져온다.
    @Override
    public List<String> getScheduledDateOnCalendar(Long marriedId, int year, int month) {
        List<String> dateList = scheduleRepository.findByMarried_IdAndYearAndMonth(marriedId, year, month).stream()
                .filter(i -> {
                    try{
                        // 실제 있는 날짜인지 확인
                        LocalDate.of(year, month, i);
                        return true;
                    }catch(DateTimeException e){
                        // 없는 날짜면 예외처리로 list에 넣지 않는다.
                        return false;
                    }
                })
                .map(i->LocalDate.of(year, month, i).toString())
                .collect(Collectors.toList());
        return dateList;
    }

    @Override
    public List<ScheduleDailyResDto> getScheduleOnDate(Long marriedId, Long userId, LocalDate date) {
        List<ScheduleDailyResDto> scheduleList = scheduleRepository.findByMarried_IdAndScheduleDate(marriedId, date).stream()
                .map(ScheduleResDto::from)
                .map(scheduleResDto -> scheduleResDto.setCurDate(date))   // curdate로
                .map(scheduleResDto ->{
                    Long memoryId = scheduleRepository.getScheduleIdById(scheduleResDto.getId());
                    boolean wroteMemory = userMemoryRepository.findByUserIdAndScheduleMemoryId(userId ,memoryId) != null;
                    return ScheduleDailyResDto.from(scheduleResDto.setCurDate(date), memoryId, wroteMemory);
                })
                .collect(Collectors.toList());

        return scheduleList;
    }
}
