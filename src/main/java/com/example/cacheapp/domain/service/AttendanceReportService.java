package com.example.cacheapp.domain.service;

import com.example.cacheapp.app.response.AttendanceReportResponse;
import com.example.cacheapp.app.response.SystemAttendanceReportResponse;
import com.example.cacheapp.domain.repository.AttendanceRepository;
import com.example.cacheapp.domain.repository.projection.AttendanceReportProjection;
import com.example.cacheapp.domain.repository.projection.AttendanceSummaryProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class AttendanceReportService {

	private final AttendanceRepository attendanceRepository;

	@Cacheable(
			value = "attendance_report",
			key = "#userId + '_' + #year + '_' + #month"
	)
	@Transactional(readOnly = true)
	public AttendanceReportResponse getMonthlyReport(Long userId, int year, int month) {
		YearMonth ym = YearMonth.of(year, month);
		LocalDateTime startDate = ym.atDay(1).atStartOfDay();
		LocalDateTime endDate = ym.plusMonths(1).atDay(1).atStartOfDay();

		AttendanceReportProjection report = attendanceRepository.getReport(userId, startDate, endDate);
		return mapToResponse(userId, year, month, report);
	}

	@Cacheable(
			value = "attendance_report",
			key = "#userId + '_' + #year + '_year'"
	)
	@Transactional(readOnly = true)
	public AttendanceReportResponse getYearlyReport(Long userId, int year) {
		LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
		LocalDateTime endDate = startDate.plusYears(1);

		AttendanceReportProjection report = attendanceRepository.getReport(userId, startDate, endDate);
		return mapToResponse(userId, year, null, report);
	}

	@Cacheable(
			value = "attendance_system_report",
			key = "'month_' + #year + '_' + #month"
	)
	public SystemAttendanceReportResponse getMonthlySystemReport(int year, int month) {

		YearMonth ym = YearMonth.of(year, month);
		LocalDateTime start = ym.atDay(1).atStartOfDay();
		LocalDateTime end = ym.plusMonths(1).atDay(1).atStartOfDay();

		var report = attendanceRepository.getSystemReport(start, end);

		return map(report, year, month);
	}

	@Cacheable(
			value = "attendance_system_report",
			key = "'year_' + #year"
	)
	public SystemAttendanceReportResponse getYearlySystemReport(int year) {

		LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
		LocalDateTime end = start.plusYears(1);

		var report = attendanceRepository.getSystemReport(start, end);

		return map(report, year, null);
	}

	private SystemAttendanceReportResponse map(
			AttendanceSummaryProjection r,
			Integer year,
			Integer month
	) {
		return SystemAttendanceReportResponse.builder()
				.year(year)
				.month(month)
				.totalUsers(r.getTotalUsers())
				.totalRecords(r.getTotalRecords())
				.totalWorkMinutes(r.getTotalWorkMinutes())
				.totalOvertimeMinutes(r.getTotalOvertimeMinutes())
				.lateDays(r.getLateDays())
				.earlyLeaveDays(r.getEarlyLeaveDays())
				.build();
	}

	private AttendanceReportResponse mapToResponse(
			Long userId,
			Integer year,
			Integer month,
			AttendanceReportProjection report
	) {
		Long totalWorkMinutes = safeLong(report.getTotalWorkMinutes());
		Long totalOvertimeMinutes = safeLong(report.getTotalOvertimeMinutes());
		Long totalRecords = safeLong(report.getTotalRecords());

		BigDecimal totalWorkHours = minutesToHours(totalWorkMinutes);
		BigDecimal totalOvertimeHours = minutesToHours(totalOvertimeMinutes);
		BigDecimal averageWorkMinutesPerDay = totalRecords == 0
				? BigDecimal.ZERO
				: BigDecimal.valueOf(totalWorkMinutes)
				.divide(BigDecimal.valueOf(totalRecords), 2, RoundingMode.HALF_UP);

		return AttendanceReportResponse.builder()
				.userId(userId)
				.year(year)
				.month(month)
				.totalRecords(totalRecords)
				.totalWorkMinutes(totalWorkMinutes)
				.totalWorkHours(totalWorkHours)
				.totalOvertimeMinutes(totalOvertimeMinutes)
				.totalOvertimeHours(totalOvertimeHours)
				.lateDays(safeLong(report.getLateDays()))
				.earlyLeaveDays(safeLong(report.getEarlyLeaveDays()))
				.presentDays(safeLong(report.getPresentDays()))
				.halfDayDays(safeLong(report.getHalfDayDays()))
				.averageWorkMinutesPerDay(averageWorkMinutesPerDay)
				.build();
	}

	private Long safeLong(Long value) {
		return value == null ? 0L : value;
	}

	private BigDecimal minutesToHours(Long minutes) {
		return BigDecimal.valueOf(minutes == null ? 0L : minutes)
				.divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
	}
}