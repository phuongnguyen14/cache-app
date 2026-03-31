package com.example.cacheapp.app.controller;

import com.example.cacheapp.app.dtos.CartDto;
import com.example.cacheapp.app.response.CartResponse;
import com.example.cacheapp.domain.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	/**
	 * GET /cart/{userId}
	 * Lấy giỏ hàng hiện tại của user
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
		return ResponseEntity.ok(cartService.getCart(userId));
	}

	/**
	 * POST /cart/add
	 * Thêm sản phẩm vào giỏ hàng (cộng dồn số lượng nếu đã có)
	 */
	@PostMapping("/add")
	public ResponseEntity<CartResponse> addToCart(@RequestBody @Valid CartDto cartDto) {
		return ResponseEntity.ok(cartService.addToCart(cartDto));
	}

	/**
	 * PUT /cart/update
	 * Cập nhật số lượng sản phẩm trong giỏ (quantity <= 0 sẽ xoá item)
	 */
	@PutMapping("/update")
	public ResponseEntity<CartResponse> updateCart(@RequestBody @Valid CartDto cartDto) {
		return ResponseEntity.ok(cartService.updateCart(cartDto));
	}
}
