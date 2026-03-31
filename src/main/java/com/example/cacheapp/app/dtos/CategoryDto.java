package com.example.cacheapp.app.dtos;

import lombok.Data;
@Data
public class CategoryDto {

	private String name;
	private String description;
	private Long parentId;

}
