package com.example.cacheapp.app.controller;

import com.example.cacheapp.app.dtos.ProductDto;
import com.example.cacheapp.app.dtos.filter.ProductParam;
import com.example.cacheapp.app.response.ProductResponse;
import com.example.cacheapp.domain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductService productService;

	@PostMapping
	public ResponseEntity<ProductResponse> create(
			@RequestBody ProductDto dto
			){
		return ResponseEntity.ok(productService.create(dto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> update(
			@PathVariable Long id,
			@RequestBody ProductDto dto
	) {
		return ResponseEntity.ok(productService.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@PathVariable Long id
	) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getById(
			@PathVariable Long id
	) {
		return ResponseEntity.ok(productService.getById(id));
	}

	@GetMapping
	public ResponseEntity<Page<ProductResponse>> getAll(
			ProductParam param,
			Pageable pageable
	){
		return ResponseEntity.ok(productService.getAll(param, pageable));
	}

}
