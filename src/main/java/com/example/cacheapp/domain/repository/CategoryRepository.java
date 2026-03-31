package com.example.cacheapp.domain.repository;

import com.example.cacheapp.app.dtos.filter.CategoryParam;
import com.example.cacheapp.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByIdAndIsDeletedFalse(Long id);

	@Query("""
    SELECT c FROM Category c
    WHERE c.isDeleted = false
      AND (:#{#param.parentId} IS NULL OR c.parentId = :#{#param.parentId})
      AND (
           :#{#param.search} IS NULL
        OR lower(c.name) LIKE concat('%', lower(:#{#param.search}), '%')
        OR lower(c.description) LIKE concat('%', lower(:#{#param.search}), '%')
      )
""")
	Page<Category> filter(CategoryParam param, Pageable pageable);
}
