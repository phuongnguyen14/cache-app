package com.example.cacheapp.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories",indexes = {
		@Index(name = "idx_category_parent", columnList = "parent_id")
})
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "parent_id")
	private Long parentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	private Category parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@Builder.Default
	private List<Category> children = new ArrayList<>();

	@Column(name = "description")
	private String description;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}