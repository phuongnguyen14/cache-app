package com.example.cacheapp.domain.service;

import com.example.cacheapp.app.dtos.CategoryDto;
import com.example.cacheapp.app.dtos.filter.CategoryParam;
import com.example.cacheapp.app.response.CategoryResponse;
import com.example.cacheapp.domain.exceptions.CustomException;
import com.example.cacheapp.domain.exceptions.ExceptionOm;
import com.example.cacheapp.domain.model.Category;
import com.example.cacheapp.domain.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	@Transactional
	public CategoryResponse create(CategoryDto dto){
		validDto(dto);
		Category category = new Category();
		category.setName(dto.getName());
		category.setDescription(dto.getDescription());
		if (dto.getParentId() != null){
			Category parent = categoryRepository.findById(dto.getParentId())
					.orElseThrow(() -> new CustomException(ExceptionOm.PARENT_CATEGORY_NOT_FOUND));
			category.setParent(parent);
		}
		category.setCreatedAt(LocalDateTime.now());
		category.setUpdatedAt(LocalDateTime.now());
		Category savedCategory = categoryRepository.save(category);
		return mapToResponse(savedCategory);
	}

	@Transactional
	public CategoryResponse update(Long id, CategoryDto dto){
		validDto(dto);
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new CustomException(ExceptionOm.CATEGORY_NOT_FOUND));
		category.setName(dto.getName());
		category.setDescription(dto.getDescription());
		if (dto.getParentId() != null){
			Category parent = categoryRepository.findById(dto.getParentId())
					.orElseThrow(() -> new CustomException(ExceptionOm.PARENT_CATEGORY_NOT_FOUND));
			category.setParent(parent);
		} else {
			category.setParent(null);
		}
		category.setUpdatedAt(LocalDateTime.now());
		Category savedCategory = categoryRepository.save(category);
		return mapToResponse(savedCategory);
	}

	@Transactional
	public void delete(Long id){
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new CustomException(ExceptionOm.CATEGORY_NOT_FOUND));
		categoryRepository.delete(category);
	}

	public CategoryResponse getById(Long id){
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new CustomException(ExceptionOm.CATEGORY_NOT_FOUND));
		return mapToResponse(category);
	}

	public Page<CategoryResponse> filter(CategoryParam param, Pageable pageable){
		Page<Category> categories = categoryRepository.filter(param, pageable);
		return categories.map(this::mapToResponse);
	}

	public void validDto(CategoryDto dto){
		if (dto.getName() == null || dto.getName().isEmpty()){
			throw new CustomException(ExceptionOm.NAME_IS_REQUIRED);
		}
	}

	public CategoryResponse mapToResponse(Category category){
		return CategoryResponse.builder()
				.id(category.getId())
				.name(category.getName())
				.description(category.getDescription())
				.parentId(category.getParent() != null ? category.getParent().getId() : null)
				.createdAt(category.getCreatedAt())
				.updatedAt(category.getUpdatedAt())
				.build();
	}


}
