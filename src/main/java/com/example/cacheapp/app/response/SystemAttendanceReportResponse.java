package com.example.cacheapp.app.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemAttendanceReportResponse {
	private Integer year;
	private Integer month;

	private Long totalUsers;
	private Long totalRecords;

	private Long totalWorkMinutes;
	private Long totalOvertimeMinutes;

	private Long lateDays;
	private Long earlyLeaveDays;
}