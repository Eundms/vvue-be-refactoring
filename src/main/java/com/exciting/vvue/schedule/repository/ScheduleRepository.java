package com.exciting.vvue.schedule.repository;

import com.exciting.vvue.schedule.model.DateType;
import com.exciting.vvue.schedule.model.Schedule;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("select sc from Schedule sc where sc.id=:id and sc.married.id=:marriedId")
    Optional<Schedule> findByIdAndMarriedId(Long id, Long marriedId);
    // 앞으로의 일정 쿼리
    @Query(value =
            "SELECT * \n" +
                    "FROM ( SELECT *, if(subquery.date_type = 'NORMAL', 1, 0) as is_normal, \n" +
                    "\t\tCASE \n" +
                    "\t\tWHEN dayofmonth(subquery.temp_date) != dayofmonth(subquery.schedule_date)\n" +
                    "\t\tTHEN CASE \n" +
                    "\t\t\tWHEN subquery.repeat_cycle = 'MONTHLY' THEN date_add(subquery.schedule_date, interval greatest(2 + timestampdiff(month, subquery.schedule_date, current_date - 1), 0) month)\n" +
                    "\t\t\tWHEN subquery.repeat_cycle = 'YEARLY' THEN date_add(subquery.schedule_date, interval greatest(5 + timestampdiff(year, subquery.schedule_date, current_date - 1), 0) year) \n" +
                    "\t\t\tELSE subquery.temp_date \n" +
                    "\t\t\tEND \n" +
                    "\t\t\tELSE subquery.temp_date \n" +
                    "\t\tEND as cur_date \n" +
                    "\t\tFROM (SELECT *, \n" +
                    "\t\t\tCASE\n" +
                    "\t\t\t\tWHEN s.repeat_cycle = 'MONTHLY' THEN date_add(s.schedule_date, interval greatest(1 + timestampdiff(month, s.schedule_date, current_date - 1) - if(s.schedule_date >= current_date - 1, 1, 0), 0) month) \n" +
                    "\t\t\t\tWHEN s.repeat_cycle = 'YEARLY' THEN date_add(s.schedule_date, interval greatest(1 + timestampdiff(year, s.schedule_date, current_date - 1) - if(s.schedule_date >= current_date - 1, 1, 0), 0) year) \n" +
                    "\t\t\t\tELSE s.schedule_date \n" +
                    "\t\t\tEND as temp_date\n" +
                    "\t\tFROM schedule s) as subquery\n" +
                    ") as subquery\n" +
                    "WHERE subquery.married_id = :marriedId\n" +
                    "AND ((repeat_cycle != 'NONREPEAT' and subquery.cur_date >= current_date) OR (subquery.cur_date >= current_date)) \n" +
                    "AND (:typeCursor < is_normal \n" +
                    "OR (:typeCursor = is_normal and ((:dateCursor < cur_date)\n" +
                    "OR (:dateCursor = cur_date and :idCursor < subquery.id))))\n" +
                    "ORDER BY is_normal, cur_date\n" +
                    "LIMIT :size", nativeQuery = true)
    List<Schedule> findByMarriedAndFuture(Long marriedId, int typeCursor, LocalDate dateCursor, long idCursor, int size);

    // 결혼ID, 연, 월로 쿼리
    @Query("select distinct dayofmonth(s.scheduleDate) from Schedule s where s.married.id = :marriedId " +
            "and (year(s.scheduleDate) < :year or (year(s.scheduleDate) = :year and month(s.scheduleDate) <= :month))" +
            "and ((s.repeatCycle = 'NONREPEAT' and year(s.scheduleDate) = :year and month(s.scheduleDate) = :month)" +
            "or (s.repeatCycle = 'YEARLY' and month(s.scheduleDate) = :month)" +
            "or s.repeatCycle = 'MONTHLY')")
    List<Integer> findByMarried_IdAndYearAndMonth(Long marriedId, int year, int month);

    // 결혼ID, 날짜로 쿼리
    @Query("select s from Schedule s where s.married.id = :marriedId " +
            "and s.scheduleDate <= :date " +
            "and ((s.repeatCycle = 'NONREPEAT' and s.scheduleDate = :date)" +
            "or (s.repeatCycle = 'YEARLY' and date_format(s.scheduleDate, '%M-%D') = date_format(:date, '%M-%D'))" +
            "or (s.repeatCycle = 'MONTHLY' and dayofmonth(s.scheduleDate) = dayofmonth(:date))) " +
            "ORDER BY case when (s.dateType = 'NORMAL') then 1 else 0 end")
    List<Schedule> findByMarried_IdAndScheduleDate(Long marriedId, LocalDate date);

    // 당일
    @Query("select s from Schedule s " +
        "where ((s.repeatCycle = 'NONREPEAT' and s.scheduleDate = :date)" +
        "or (s.repeatCycle = 'YEARLY' and date_format(s.scheduleDate, '%M-%D') = date_format(:date, '%M-%D'))" +
        "or (s.repeatCycle = 'MONTHLY' and dayofmonth(s.scheduleDate) = dayofmonth(:date))) " +
        "ORDER BY case when (s.dateType = 'NORMAL') then 1 else 0 end")
    List<Schedule> findByScheduleDate(LocalDate date);


    @Query("select sm.id from Schedule s left join ScheduleMemory sm on s.id = sm.scheduleId where s.id = :id")
    Long getScheduleIdById(Long id);
}
