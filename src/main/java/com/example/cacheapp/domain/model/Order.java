package com.example.cacheapp.domain.model;

import com.example.cacheapp.domain.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders", indexes = {
		@Index(name = "idx_order_user", columnList =  "user_id"),
})
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "status", columnDefinition = "varchar")
	@Enumerated(EnumType.STRING)
	private OrderStatus status = OrderStatus.PENDING;

	@Column(name = "shipping_address")
	private String shippingAddress;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}

