package com.example.cacheapp.domain.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Bắt CustomException — lỗi nghiệp vụ có mã và message chuẩn
	 */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("success", false);
		body.put("message", ex.getExceptionOm().val());
		body.put("code", ex.getExceptionOm().name());
		return ResponseEntity.status(ex.getExceptionOm().getStatus()).body(body);
	}

	/**
	 * Bắt lỗi @Valid / @Validated — lỗi validation từ DTO
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("success", false);
		body.put("message", "Dữ liệu đầu vào không hợp lệ");

		Map<String, String> fieldErrors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
				fieldErrors.put(error.getField(), error.getDefaultMessage())
		);
		body.put("errors", fieldErrors);
		return ResponseEntity.badRequest().body(body);
	}

	/**
	 * Bắt mọi lỗi còn lại chưa được xử lý cụ thể
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("success", false);
		body.put("message", "Lỗi hệ thống, vui lòng thử lại sau");
		return ResponseEntity.internalServerError().body(body);
	}
}
