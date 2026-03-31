package com.example.cacheapp.domain.service;

import com.example.cacheapp.app.dtos.CartDto;
import com.example.cacheapp.app.response.CartResponse;
import com.example.cacheapp.domain.exceptions.CustomException;
import com.example.cacheapp.domain.exceptions.ExceptionOm;
import com.example.cacheapp.domain.model.Cart;
import com.example.cacheapp.domain.model.CartItem;
import com.example.cacheapp.domain.model.Product;
import com.example.cacheapp.domain.model.enums.CartStatus;
import com.example.cacheapp.domain.repository.CartItemRepository;
import com.example.cacheapp.domain.repository.CartRepository;
import com.example.cacheapp.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	private static final Set<CartStatus> EDITABLE = Set.of(CartStatus.ACTIVE, CartStatus.PENDING);

	public CartResponse getCart(Long userId) {
		Cart cart = cartRepository.findByUserIdAndStatusIn(userId, EDITABLE)
				.orElseThrow(() -> new CustomException(ExceptionOm.CART_NOT_FOUND));
		return mapToResponse(cart, cartItemRepository.findByCartId(cart.getId()));
	}

	@Transactional
	public CartResponse addToCart(CartDto cartDto) {
		Long userId = cartDto.getUserId();

		// 1. Lấy giỏ hàng đang active hoặc tạo mới nếu chưa có
		Cart cart = cartRepository.findByUserIdAndStatusIn(userId, EDITABLE)
				.orElseGet(() -> {
					Cart newCart = new Cart();
					newCart.setUserId(userId);
					newCart.setStatus(CartStatus.PENDING);
					newCart.setCreatedAt(LocalDateTime.now());
					newCart.setUpdatedAt(LocalDateTime.now());
					return cartRepository.save(newCart);
				});

		if (cartDto.getItems() != null && !cartDto.getItems().isEmpty()) {
			for (CartDto.CartItemDto itemDto : cartDto.getItems()) {
				Long productId = itemDto.getProductId();
				Integer quantity = itemDto.getQuantity();

				Product product = productRepository.findByIdAndIsDeletedFalse(productId)
						.orElseThrow(() -> new CustomException(ExceptionOm.PRODUCT_NOT_FOUND));

				if (product.getStock() < quantity) {
					throw new CustomException(ExceptionOm.INSUFFICIENT_STOCK);
				}

				// Dùng query có sẵn thay vì stream().filter() → tối ưu N+1
				CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
						.orElseGet(() -> {
							CartItem newItem = new CartItem();
							newItem.setCartId(cart.getId());
							newItem.setProductId(productId);
							newItem.setQuantity(0);
							newItem.setCreatedAt(LocalDateTime.now());
							return newItem;
						});

				// Cộng dồn số lượng
				cartItem.setQuantity(cartItem.getQuantity() + quantity);
				cartItem.setPriceAtTime(product.getPrice());
				cartItem.setUpdatedAt(LocalDateTime.now());
				cartItemRepository.save(cartItem);
			}
		}
		return mapToResponse(cart, cartItemRepository.findByCartId(cart.getId()));
	}

	@Transactional
	public CartResponse updateCart(CartDto cartDto) {
		Long userId = cartDto.getUserId();
		Cart cart = cartRepository.findByUserIdAndStatusIn(userId, EDITABLE)
				.orElseThrow(() -> new CustomException(ExceptionOm.CART_NOT_FOUND));

		if (cartDto.getItems() != null) {
			for (CartDto.CartItemDto itemDto : cartDto.getItems()) {
				Long productId = itemDto.getProductId();
				Integer newQuantity = itemDto.getQuantity();

				// Dùng query có sẵn thay vì stream().filter()
				CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
						.orElseThrow(() -> new CustomException(ExceptionOm.ITEM_NOT_FOUND_IN_CART));

				if (newQuantity <= 0) {
					cartItemRepository.delete(cartItem);
				} else {
					cartItem.setQuantity(newQuantity);
					cartItem.setUpdatedAt(LocalDateTime.now());
					cartItemRepository.save(cartItem);
				}
			}
		}
		return mapToResponse(cart, cartItemRepository.findByCartId(cart.getId()));
	}

	public CartResponse mapToResponse(Cart cart, List<CartItem> items) {
		double total = items == null ? 0d : items.stream()
				.mapToDouble(ci -> (ci.getPriceAtTime() == null ? 0d : ci.getPriceAtTime())
						* (ci.getQuantity() == null ? 0 : ci.getQuantity()))
				.sum();

		List<CartResponse.CartItemResponse> itemResponses = items == null ? List.of() : items.stream()
				.map(ci -> CartResponse.CartItemResponse.builder()
						.productId(ci.getProductId())
						.productName(ci.getProduct() != null ? ci.getProduct().getName() : null)
						.quantity(ci.getQuantity())
						.priceAtTime(ci.getPriceAtTime())
						.build())
				.toList();

		return CartResponse.builder()
				.id(cart.getId())
				.userId(cart.getUserId())
				.status(cart.getStatus())
				.items(itemResponses)
				.totalPrice(total)
				.createdAt(cart.getCreatedAt())
				.updatedAt(cart.getUpdatedAt())
				.build();
	}

}
