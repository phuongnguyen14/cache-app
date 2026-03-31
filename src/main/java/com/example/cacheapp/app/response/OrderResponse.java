package com.example.cacheapp.app.response;

import com.example.cacheapp.domain.model.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class OrderResponse {
	private Long id;
	private Long userId;
	private Double totalAmount;
	private OrderStatus status;
	private String shippingAddress;
	private List<OrderItemResponse> items;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Builder
	@Data
	public static class OrderItemResponse {
		private Long productId;
		private String productName;
		private Integer quantity;
		private Double price;
	}
}
