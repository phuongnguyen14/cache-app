package com.example.cacheapp.app.dtos.filter;

import lombok.Data;

@Data
public class ProductParam {
	private String search;
	private Long categoryId;
	private Double minPrice;
	private Double maxPrice;

}
