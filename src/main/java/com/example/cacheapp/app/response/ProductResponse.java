package com.example.cacheapp.app.response;

import com.example.cacheapp.domain.model.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
	private Long id;
	private String name;
	private String code;
	private String description;
	private Long categoryId;
	private Double price;
	private Integer stock;
	private Category category;
	private Boolean isDeleted;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
}
