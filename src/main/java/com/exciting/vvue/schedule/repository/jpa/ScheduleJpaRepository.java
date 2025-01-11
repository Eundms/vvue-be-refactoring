package com.exciting.vvue.schedule.repository.jpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.exciting.vvue.schedule.model.Schedule;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {
	@Query("select sc from Schedule sc where sc.id=:id and sc.married.id=:marriedId")
	Optional<Schedule> findByIdAndMarriedId(Long id, Long marriedId);

	// 앞으로의 일정 쿼리
	@Query(value =
		"""
   			SELECT *
			FROM (
			    SELECT *,
			           ROW_NUMBER() OVER (ORDER BY is_normal, cur_date) AS rownum
			    FROM (
			        SELECT *,
			               IF(subquery.date_type = 'NORMAL', 1, 0) AS is_normal,
			               CASE
			                   WHEN DAYOFMONTH(subquery.temp_date) != DAYOFMONTH(subquery.schedule_date)
			                   THEN
			                       CASE
			                           WHEN subquery.repeat_cycle = 'MONTHLY'
			                           THEN DATE_ADD(subquery.schedule_date, INTERVAL GREATEST(2 + TIMESTAMPDIFF(MONTH, subquery.schedule_date, CURRENT_DATE - 1), 0) MONTH)
			                           WHEN subquery.repeat_cycle = 'YEARLY'
			                           THEN DATE_ADD(subquery.schedule_date, INTERVAL GREATEST(5 + TIMESTAMPDIFF(YEAR, subquery.schedule_date, CURRENT_DATE - 1), 0) YEAR)
			                           ELSE subquery.temp_date
			                       END
			                   ELSE subquery.temp_date
			               END AS cur_date
			        FROM (
			            SELECT *,
			                   CASE
			                       WHEN s.repeat_cycle = 'MONTHLY'
			                       THEN DATE_ADD(s.schedule_date, INTERVAL GREATEST(1 + TIMESTAMPDIFF(MONTH, s.schedule_date, CURRENT_DATE - 1) - IF(s.schedule_date >= CURRENT_DATE - 1, 1, 0), 0) MONTH)
			                       WHEN s.repeat_cycle = 'YEARLY'
			                       THEN DATE_ADD(s.schedule_date, INTERVAL GREATEST(1 + TIMESTAMPDIFF(YEAR, s.schedule_date, CURRENT_DATE - 1) - IF(s.schedule_date >= CURRENT_DATE - 1, 1, 0), 0) YEAR)
			                       ELSE s.schedule_date
			                   END AS temp_date
			            FROM schedule s
			        ) AS subquery
			    ) AS subquery
			    WHERE subquery.married_id = :marriedId
			      AND (
			          (repeat_cycle != 'NONREPEAT' AND subquery.cur_date >= CURRENT_DATE)
			          OR (subquery.cur_date >= CURRENT_DATE)
			      )
			      AND (
			          (:typeCursor < is_normal)
			          OR (:typeCursor = is_normal AND (
			              (:dateCursor < cur_date)
			              OR (:dateCursor = cur_date AND :idCursor < subquery.id)
			          ))
			      )
			) AS numbered
			WHERE rownum <= :resultSize
			ORDER BY is_normal, cur_date;
			""", nativeQuery = true)
	List<Schedule> findByMarriedAndFuture(Long marriedId, int typeCursor, LocalDate dateCursor, long idCursor,
		int resultSize);

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

	@Query("select sm.id from Schedule s left join ScheduleMemory sm on s.id = sm.scheduleId "
		+ "where s.id = :id and sm.scheduleDate = :scheduleDate")
	Long getScheduleMemoryIdByIdAndScheduleDate(Long id, LocalDate scheduleDate);
}
