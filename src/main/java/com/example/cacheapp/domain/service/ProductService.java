package com.example.cacheapp.domain.service;

import com.example.cacheapp.app.dtos.ProductDto;
import com.example.cacheapp.app.dtos.filter.ProductParam;
import com.example.cacheapp.app.response.ProductResponse;
import com.example.cacheapp.domain.exceptions.CustomException;
import com.example.cacheapp.domain.exceptions.ExceptionOm;
import com.example.cacheapp.domain.model.Category;
import com.example.cacheapp.domain.model.Product;
import com.example.cacheapp.domain.repository.CategoryRepository;
import com.example.cacheapp.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	@Transactional
	public ProductResponse create(ProductDto dto) {
		validDto(dto);
		Product product = new Product();
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setCode(dto.getCode());
		product.setPrice(dto.getPrice());
		product.setStock(dto.getStock());
		product.setCategoryId(dto.getCategoryId());
		product.setIsDeleted(false);
		product.setCreatedAt(LocalDateTime.now());
		product.setUpdatedAt(LocalDateTime.now());

		Product savedProduct = productRepository.save(product);
		return mapToResponse(savedProduct);
	}

	@CacheEvict(value = "products", key = "#id")
	@Transactional
	public ProductResponse update(Long id, ProductDto dto) {
		Product product = productRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new CustomException(ExceptionOm.PRODUCT_NOT_FOUND));

		if (!product.getCode().equals(dto.getCode())) {
			validDto(dto);
		}

		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setCode(dto.getCode());
		product.setPrice(dto.getPrice());
		product.setStock(dto.getStock());
		product.setCategoryId(dto.getCategoryId());
		product.setUpdatedAt(LocalDateTime.now());

		Product updatedProduct = productRepository.save(product);
		return mapToResponse(updatedProduct);
	}

	@CacheEvict(value = "products", key = "#id")
	@Transactional
	public void delete(Long id) {
		Product product = productRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new CustomException(ExceptionOm.PRODUCT_NOT_FOUND));
		product.setIsDeleted(true);
		product.setUpdatedAt(LocalDateTime.now());
		productRepository.save(product);

	}

	@Cacheable(value = "products", key = "#id")
	public ProductResponse getById(Long id) {
		Product product = productRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new CustomException(ExceptionOm.PRODUCT_NOT_FOUND));
		return mapToResponse(product);
	}

	public Page<ProductResponse> getAll(ProductParam param, Pageable pageable) {
		Page<Product> products = productRepository.filter(param, pageable);
		return products.map(this::mapToResponse);
	}


	public void validDto(ProductDto dto) {
		if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
			throw new CustomException(ExceptionOm.PRODUCT_CODE_IS_REQUIRED);
		}
		if(productRepository.existsByCodeAndIsDeletedFalse(dto.getCode())) {
			throw new CustomException(ExceptionOm.PRODUCT_CODE_ALREADY_EXISTS);
		}
	}


	public ProductResponse mapToResponse(Product product) {
		Category category = categoryRepository.findByIdAndIsDeletedFalse(product.getCategoryId())
				.orElse(null);
		return ProductResponse.builder()
				.name(product.getName())
				.code(product.getCode())
				.description(product.getDescription())
				.price(product.getPrice())
				.stock(product.getStock())
				.categoryId(product.getCategoryId())
				.category(category)
				.isDeleted(product.getIsDeleted())
				.createdAt(product.getCreatedAt())
				.updatedAt(product.getUpdatedAt())
				.build();
	}

}
