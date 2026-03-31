package com.example.cacheapp.domain.repository;

import com.example.cacheapp.domain.model.Attendance;
import com.example.cacheapp.domain.repository.projection.AttendanceReportProjection;
import com.example.cacheapp.domain.repository.projection.AttendanceSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	@Query("""
        select
            coalesce(sum(coalesce(a.workMinutes, 0)), 0) as totalWorkMinutes,
            coalesce(sum(coalesce(a.overtimeMinutes, 0)), 0) as totalOvertimeMinutes,
            coalesce(sum(case when a.checkInStatus = com.example.cacheapp.domain.model.enums.CheckStatus.LATE then 1 else 0 end), 0) as lateDays,
            coalesce(sum(case when a.checkOutStatus = com.example.cacheapp.domain.model.enums.CheckStatus.EARLY_LEAVE then 1 else 0 end), 0) as earlyLeaveDays,
            coalesce(sum(case when a.workDayStatus = com.example.cacheapp.domain.model.enums.WorkDayStatus.FULL_DAY then 1 else 0 end), 0) as presentDays,
            coalesce(sum(case when a.workDayStatus = com.example.cacheapp.domain.model.enums.WorkDayStatus.HALF_DAY then 1 else 0 end), 0) as halfDayDays,
            count(a) as totalRecords
        from Attendance a
        where a.userId = :userId
          and a.isDeleted = false
          and a.workDate >= :startDate
          and a.workDate < :endDate
    """)
	AttendanceReportProjection getReport(
			@Param("userId") Long userId,
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate
	);

	@Query("""
    select
        count(distinct a.userId) as totalUsers,
        coalesce(sum(coalesce(a.workMinutes, 0)), 0) as totalWorkMinutes,
        coalesce(sum(coalesce(a.overtimeMinutes, 0)), 0) as totalOvertimeMinutes,
        coalesce(sum(case when a.checkInStatus = com.example.cacheapp.domain.model.enums.CheckStatus.LATE then 1 else 0 end), 0) as lateDays,
        coalesce(sum(case when a.checkOutStatus = com.example.cacheapp.domain.model.enums.CheckStatus.EARLY_LEAVE then 1 else 0 end), 0) as earlyLeaveDays,
        count(a) as totalRecords
    from Attendance a
    where a.isDeleted = false
      and a.workDate >= :startDate
      and a.workDate < :endDate
""")
	AttendanceSummaryProjection getSystemReport(
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate
	);
}
