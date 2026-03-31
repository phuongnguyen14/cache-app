package com.example.cacheapp.app.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceReportResponse {
	private Long userId;
	private Integer year;
	private Integer month;

	private Long totalRecords;
	private Long totalWorkMinutes;
	private BigDecimal totalWorkHours;

	private Long totalOvertimeMinutes;
	private BigDecimal totalOvertimeHours;

	private Long lateDays;
	private Long earlyLeaveDays;
	private Long presentDays;
	private Long halfDayDays;

	private BigDecimal averageWorkMinutesPerDay;
}