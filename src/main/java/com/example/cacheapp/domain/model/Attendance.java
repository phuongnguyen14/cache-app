package com.example.cacheapp.domain.model;

import com.example.cacheapp.domain.model.enums.CheckStatus;
import com.example.cacheapp.domain.model.enums.WorkDayStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendances")
public class Attendance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;

	@Column(name = "work_date")
	private LocalDateTime workDate;

	@Column(name = "check_in")
	private Timestamp checkIn;

	@Column(name = "check_out")
	private Timestamp checkOut;

	@Column(name = "work_minutes")
	private Integer workMinutes;

	@Column(name = "overtime_minutes")
	private Integer overtimeMinutes;

	@Column(name = "check_in_status", columnDefinition = "varchar")
	@Enumerated(EnumType.STRING)
	private CheckStatus checkInStatus;

	@Column(name = "check_out_status", columnDefinition = "varchar")
	@Enumerated(EnumType.STRING)
	private CheckStatus checkOutStatus;

	@Column(name = "work_day_status", columnDefinition = "varchar")
	@Enumerated(EnumType.STRING)
	private WorkDayStatus workDayStatus;

	@Column(name = "note")
	private String note;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;


}
