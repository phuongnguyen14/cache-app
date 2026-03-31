package com.example.cacheapp.app.dtos.filter;

import com.example.cacheapp.domain.model.enums.Gender;
import com.example.cacheapp.domain.model.enums.UserState;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
public class UserParam {
	private String search;
	private Gender gender;
	private UserState state;
	private LocalDateTime dateOfBirthFrom;
	private LocalDateTime dateOfBirthTo;
	public String getSearch() {
		return StringUtils.hasLength(search) ? search.toLowerCase() : null;
	}
}
