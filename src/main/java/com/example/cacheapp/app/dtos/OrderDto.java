package com.example.cacheapp.app.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

	@NotNull(message = "Id người dùng không được bỏ trống")
	private Long userId;

	@NotBlank(message = "Địa chỉ giao hàng không được bỏ trống")
	private String shippingAddress;

}
