package com.example.cacheapp.domain.repository.projection;

public interface AttendanceReportProjection {
	Long getTotalWorkMinutes();
	Long getTotalOvertimeMinutes();
	Long getLateDays();
	Long getEarlyLeaveDays();
	Long getPresentDays();
	Long getHalfDayDays();
	Long getTotalRecords();
}