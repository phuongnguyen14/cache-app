package com.example.cacheapp.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items", indexes = {
		@Index( name = "idx_order_item_order_id", columnList = "order_id"),
		@Index( name = "idx_order_item_product_id", columnList = "product_id")
})
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_id")
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", insertable = false, updatable = false)
	private Order order;

	@Column(name = "product_id")
	private Long productId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "price")
	private Double price;


}
