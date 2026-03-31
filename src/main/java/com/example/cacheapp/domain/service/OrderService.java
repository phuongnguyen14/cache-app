package com.example.cacheapp.domain.service;

import com.example.cacheapp.app.dtos.OrderDto;
import com.example.cacheapp.app.response.OrderResponse;
import com.example.cacheapp.domain.exceptions.CustomException;
import com.example.cacheapp.domain.exceptions.ExceptionOm;
import com.example.cacheapp.domain.model.*;
import com.example.cacheapp.domain.model.enums.CartStatus;
import com.example.cacheapp.domain.model.enums.OrderStatus;
import com.example.cacheapp.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	private static final Set<CartStatus> EDITABLE = Set.of(CartStatus.ACTIVE, CartStatus.PENDING);

	/**
	 * Checkout: Chuyển giỏ hàng thành đơn hàng, trừ kho, xoá giỏ hàng
	 */
	@Transactional
	public OrderResponse checkout(OrderDto orderDto) {
		Long userId = orderDto.getUserId();

		// 1. Lấy giỏ hàng đang active
		Cart cart = cartRepository.findByUserIdAndStatusIn(userId, EDITABLE)
				.orElseThrow(() -> new CustomException(ExceptionOm.CART_NOT_FOUND));

		List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
		if (cartItems.isEmpty()) {
			throw new CustomException(ExceptionOm.CART_EMPTY);
		}

		// 2. Tính tổng tiền
		double totalAmount = cartItems.stream()
				.mapToDouble(item -> (item.getPriceAtTime() != null ? item.getPriceAtTime() : 0d)
						* (item.getQuantity() != null ? item.getQuantity() : 0))
				.sum();

		// 3. Tạo Order
		Order order = new Order();
		order.setUserId(userId);
		order.setTotalAmount(totalAmount);
		order.setStatus(OrderStatus.PENDING);
		order.setShippingAddress(orderDto.getShippingAddress());
		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());
		Order savedOrder = orderRepository.save(order);

		// 4. Tạo OrderItems và trừ kho
		List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
			Product product = productRepository.findByIdAndIsDeletedFalse(cartItem.getProductId())
					.orElseThrow(() -> new CustomException(ExceptionOm.PRODUCT_NOT_FOUND));

			if (product.getStock() < cartItem.getQuantity()) {
				throw new CustomException(ExceptionOm.INSUFFICIENT_STOCK);
			}

			product.setStock(product.getStock() - cartItem.getQuantity());
			productRepository.save(product);

			OrderItem orderItem = new OrderItem();
			orderItem.setOrderId(savedOrder.getId());
			orderItem.setProductId(cartItem.getProductId());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setPrice(cartItem.getPriceAtTime());
			return orderItem;
		}).collect(Collectors.toList());

		List<OrderItem> savedItems = orderItemRepository.saveAll(orderItems);

		// 5. Xoá giỏ hàng sau khi đã checkout thành công
		cartItemRepository.deleteAll(cartItems);
		cartRepository.delete(cart);

		return mapToResponse(savedOrder, savedItems);
	}

	/**
	 * Huỷ đơn hàng — chỉ cho phép huỷ khi còn PENDING
	 */
	@Transactional
	public OrderResponse cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new CustomException(ExceptionOm.ORDER_NOT_FOUND));

		if (order.getStatus() != OrderStatus.PENDING) {
			throw new CustomException(ExceptionOm.ORDER_CANNOT_CANCEL);
		}

		// Hoàn trả kho
		List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
		items.forEach(item -> {
			productRepository.findByIdAndIsDeletedFalse(item.getProductId()).ifPresent(product -> {
				product.setStock(product.getStock() + item.getQuantity());
				productRepository.save(product);
			});
		});

		order.setStatus(OrderStatus.CANCELLED);
		order.setUpdatedAt(LocalDateTime.now());
		orderRepository.save(order);

		return mapToResponse(order, items);
	}

	/**
	 * Lấy danh sách đơn hàng của user
	 */
	public List<OrderResponse> getUserOrders(Long userId) {
		return orderRepository.findByUserId(userId).stream()
				.map(order -> mapToResponse(order, orderItemRepository.findByOrderId(order.getId())))
				.toList();
	}

	/**
	 * Lấy chi tiết 1 đơn hàng
	 */
	public OrderResponse getOrderById(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new CustomException(ExceptionOm.ORDER_NOT_FOUND));
		return mapToResponse(order, orderItemRepository.findByOrderId(orderId));
	}

	public OrderResponse mapToResponse(Order order, List<OrderItem> orderItems) {
		List<OrderResponse.OrderItemResponse> itemResponses = orderItems == null ? List.of() : orderItems.stream()
				.map(item -> OrderResponse.OrderItemResponse.builder()
						.productId(item.getProductId())
						.productName(item.getProduct() != null ? item.getProduct().getName() : null)
						.quantity(item.getQuantity())
						.price(item.getPrice())
						.build())
				.toList();

		return OrderResponse.builder()
				.id(order.getId())
				.userId(order.getUserId())
				.totalAmount(order.getTotalAmount())
				.status(order.getStatus())
				.shippingAddress(order.getShippingAddress())
				.items(itemResponses)
				.createdAt(order.getCreatedAt())
				.updatedAt(order.getUpdatedAt())
				.build();
	}
}
