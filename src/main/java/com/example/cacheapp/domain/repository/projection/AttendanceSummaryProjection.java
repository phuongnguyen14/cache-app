package com.example.cacheapp.domain.repository.projection;

public interface AttendanceSummaryProjection {
	Long getTotalUsers();
	Long getTotalWorkMinutes();
	Long getTotalOvertimeMinutes();
	Long getLateDays();
	Long getEarlyLeaveDays();
	Long getTotalRecords();
}
