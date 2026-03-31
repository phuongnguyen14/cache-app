package com.example.cacheapp.app.dtos;

import lombok.Data;

@Data
public class ProductDto {
	private String name;
	private String code;
	private String description;
	private Double price;
	private Integer stock;
	private Long categoryId;
}
