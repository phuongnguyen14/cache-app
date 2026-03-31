package com.example.cacheapp.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products", indexes = {
		@Index(name = "idx_product_category", columnList = "category_id"),
		@Index(name = "idx_product_name", columnList = "name")
})
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "code", unique = true)
	private String code;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private Double price;

	@Column(name = "stock")
	private Integer stock;

	@Column(name = "category_id")
	private Long categoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", insertable = false, updatable = false)
	private Category category;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;




}
