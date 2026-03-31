package com.example.cacheapp.app.controller;

import com.example.cacheapp.app.dtos.CategoryDto;
import com.example.cacheapp.app.dtos.filter.CategoryParam;
import com.example.cacheapp.app.response.CategoryResponse;
import com.example.cacheapp.domain.model.Category;
import com.example.cacheapp.domain.service.CategoryService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@PostMapping
	public ResponseEntity<CategoryResponse> create(
			CategoryDto dto
	){
		return ResponseEntity.ok(categoryService.create(dto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryResponse> update(
			@PathVariable Long id,
			@RequestBody CategoryDto dto
	) {
		return ResponseEntity.ok(categoryService.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@PathVariable Long id
	) {
		categoryService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> getById(
			@PathVariable Long id
	) {
		return ResponseEntity.ok(categoryService.getById(id));
	}

	@GetMapping
	public Page<ResponseEntity<CategoryResponse>> getAll(
			CategoryParam param,
			Pageable pageable
	) {
		return categoryService.filter(param, pageable)
				.map(ResponseEntity::ok);
	}

}
