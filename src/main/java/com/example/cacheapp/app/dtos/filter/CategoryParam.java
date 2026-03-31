package com.example.cacheapp.app.dtos.filter;

import lombok.Data;

@Data
public class CategoryParam {
	private String search;
	private Long parentId;

}
