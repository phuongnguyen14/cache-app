package com.example.cacheapp.app.response;

import com.example.cacheapp.domain.model.enums.Gender;
import com.example.cacheapp.domain.model.enums.UserState;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private Long id;
	private String username;
	private Gender gender;
	private LocalDateTime dateOfBirth;
	private UserState state;
	private Boolean isDeleted;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
