package com.example.cacheapp.app.controller;

import com.example.cacheapp.app.response.AttendanceReportResponse;
import com.example.cacheapp.app.response.SystemAttendanceReportResponse;
import com.example.cacheapp.domain.service.AttendanceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistic")
public class AttendanceReportController {
	@Autowired
	private AttendanceReportService attendanceReportService;

	@GetMapping("/users/{userId}/monthly")
	public AttendanceReportResponse monthly(
			@PathVariable Long userId,
			@RequestParam int year,
			@RequestParam int month
	) {
		return attendanceReportService.getMonthlyReport(userId, year, month);
	}

	@GetMapping("/users/{userId}/yearly")
	public AttendanceReportResponse yearly(
			@PathVariable Long userId,
			@RequestParam int year
	) {
		return attendanceReportService.getYearlyReport(userId, year);
	}

	@GetMapping("/reports/monthly")
	public SystemAttendanceReportResponse monthly(
			@RequestParam int year,
			@RequestParam int month
	) {
		return attendanceReportService.getMonthlySystemReport(year, month);
	}

	@GetMapping("/reports/yearly")
	public SystemAttendanceReportResponse yearly(
			@RequestParam int year
	) {
		return attendanceReportService.getYearlySystemReport(year);
	}
}
