package com.example.cacheapp.domain.repository;

import com.example.cacheapp.app.dtos.filter.ProductParam;
import com.example.cacheapp.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	boolean existsByCodeAndIsDeletedFalse(String code);

	Optional<Product> findByCodeAndIsDeletedFalse(String code);

	Optional<Product> findByIdAndIsDeletedFalse(Long id);

	@Query("""
			    SELECT p FROM Product p
			    WHERE
			        (:#{#param.categoryId} IS NULL OR p.categoryId = :#{#param.categoryId})
			        AND (:#{#param.search} IS NULL
			             OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#param.search}, '%'))
			             OR LOWER(p.code) LIKE LOWER(CONCAT('%', :#{#param.search}, '%')))
			        AND (:#{#param.minPrice} IS NULL OR p.price >= :#{#param.minPrice})
			        AND (:#{#param.maxPrice} IS NULL OR p.price <= :#{#param.maxPrice})
			        AND p.isDeleted = false
			""")
	Page<Product> filter(ProductParam param, Pageable pageable);
}
