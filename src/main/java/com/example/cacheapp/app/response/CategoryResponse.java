package com.example.cacheapp.app.response;


import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
	private Long id;
	private String name;
	private String description;
	private Long parentId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
