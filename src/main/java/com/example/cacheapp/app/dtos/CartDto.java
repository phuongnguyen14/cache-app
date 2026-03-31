package com.example.cacheapp.app.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
	@NotNull
	private Long userId;
	private List<CartItemDto> items;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CartItemDto {
		@NotNull
		private Long productId;
		@Min(1)
		private Integer quantity;
	}

}
