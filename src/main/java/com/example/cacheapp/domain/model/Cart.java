package com.example.cacheapp.domain.model;

import com.example.cacheapp.domain.model.enums.CartStatus;
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
@Table(name = "carts",  indexes = {
		@Index(name = "idx_cart_user", columnList = "user_id")
})
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;

	@Column(name = "status", columnDefinition = "varchar")
	@Enumerated(EnumType.STRING)
	private CartStatus status = CartStatus.PENDING;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
