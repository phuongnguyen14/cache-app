package com.example.cacheapp.domain.exceptions;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final ExceptionOm exceptionOm;

	public CustomException(ExceptionOm exceptionOm) {
		super(exceptionOm.val());
		this.exceptionOm = exceptionOm;
	}
}
