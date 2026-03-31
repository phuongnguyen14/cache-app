package com.example.cacheapp.app.dtos;

import com.example.cacheapp.domain.model.enums.UserState;
import lombok.Data;

@Data
public class ChangeStateDto {
	private UserState state;
}
