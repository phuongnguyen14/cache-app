package com.example.cacheapp.app.response;

import com.example.cacheapp.domain.model.enums.CartStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class CartResponse {
	private Long id;
	private Long userId;
	private CartStatus status;
	private List<CartItemResponse> items;
	private Double totalPrice;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Builder
	@Data
	public static class CartItemResponse {
		private Long productId;
		private String productName;
		private Integer quantity;
		private Double priceAtTime;
	}

}
