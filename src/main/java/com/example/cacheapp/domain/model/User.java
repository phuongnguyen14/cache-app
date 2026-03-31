package com.example.cacheapp.domain.model;

import com.example.cacheapp.domain.model.enums.Gender;
import com.example.cacheapp.domain.model.enums.UserState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "username")
	private String userName;
	@Column(name = "date_of_birth")
	private LocalDateTime dateOfBirth;
	@Column(name = "gender",columnDefinition = "varchar")
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Column(name = "user_state", columnDefinition = "varchar")
	@Enumerated(EnumType.STRING)
	private UserState userState = UserState.ACTIVE;
	@Column(name = "is_deleted")
	private Boolean isDeleted =false;
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}
