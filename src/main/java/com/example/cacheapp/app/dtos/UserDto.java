package com.example.cacheapp.app.dtos;

import com.example.cacheapp.domain.model.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto {
	private Long id;
	private String username;
	private Gender gender;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateOfBirth;

}
