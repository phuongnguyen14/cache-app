package com.example.cacheapp.app.controller;

import com.example.cacheapp.app.dtos.OrderDto;
import com.example.cacheapp.app.response.OrderResponse;
import com.example.cacheapp.domain.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	/**
	 * POST /orders/checkout
	 * Thanh toán giỏ hàng → tạo đơn hàng mới
	 */
	@PostMapping("/checkout")
	public ResponseEntity<OrderResponse> checkout(@RequestBody @Valid OrderDto orderDto) {
		return ResponseEntity.ok(orderService.checkout(orderDto));
	}

	/**
	 * GET /orders/{orderId}
	 * Lấy chi tiết một đơn hàng
	 */
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderService.getOrderById(orderId));
	}

	/**
	 * GET /orders/user/{userId}
	 * Lấy lịch sử đơn hàng của một user
	 */
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable Long userId) {
		return ResponseEntity.ok(orderService.getUserOrders(userId));
	}

	/**
	 * PATCH /orders/{orderId}/cancel
	 * Huỷ đơn hàng — chỉ cho phép khi còn PENDING, tự động hoàn trả kho
	 */
	@PatchMapping("/{orderId}/cancel")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderService.cancelOrder(orderId));
	}
}
