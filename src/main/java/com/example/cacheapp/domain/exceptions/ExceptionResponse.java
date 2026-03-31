package com.example.cacheapp.domain.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponse {
	private String description;
	private String message;
	private String messageCode;
	private String path;
	private int statusCode;
	private long timestamp;
}
