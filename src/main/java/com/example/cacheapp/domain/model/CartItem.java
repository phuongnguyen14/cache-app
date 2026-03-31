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
@Table(name = "cart_items", indexes =
		{
				@Index(name = "idx_cart_item_cart", columnList = "cart_id"),
				@Index(name = "idx_cart_item_product", columnList = "product_id")
		})
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "cart_id", nullable = false)
	private Long cartId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", insertable = false, updatable = false)
	private Cart cart;

	@Column(name = "product_id")
	private Long productId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "price_at_time")
	private Double priceAtTime;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;


}
