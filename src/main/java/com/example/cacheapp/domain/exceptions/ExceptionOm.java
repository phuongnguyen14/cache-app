package com.example.cacheapp.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionOm {

	// Mặc định cho lỗi hệ thống chung
	INTERNAL_SERVER_ERROR("Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),

	// Auth & User
	PASSWORD_REQUIRE("Mật khẩu là bắt buộc", HttpStatus.BAD_REQUEST),
	EMAIL_ALREADY_EXISTS("Email đã được sử dụng", HttpStatus.CONFLICT),
	USER_NOT_FOUND("Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
	UNAUTHORIZED("Không có quyền truy cập", HttpStatus.UNAUTHORIZED),
	USERNAME_ALREADY_EXISTS("Tên đăng nhập đã tồn tại", HttpStatus.CONFLICT),
	USERNAME_IS_REQUIRED("Tên đăng nhập là bắt buộc", HttpStatus.BAD_REQUEST),
	PRODUCT_CODE_IS_REQUIRED("Mã sản phẩm là bắt buộc", HttpStatus.BAD_REQUEST),
	PRODUCT_CODE_ALREADY_EXISTS("Mã sản phẩm đã tồn tại", HttpStatus.CONFLICT),
	PRODUCT_NOT_FOUND("Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND),
	NAME_IS_REQUIRED("Tên là bắt buộc", HttpStatus.BAD_REQUEST),
	PARENT_CATEGORY_NOT_FOUND("Không tìm thấy danh mục cha", HttpStatus.NOT_FOUND),
	CATEGORY_NOT_FOUND("Không tìm thấy danh mục", HttpStatus.NOT_FOUND),
	INSUFFICIENT_STOCK("Số lượng tồn kho không đủ", HttpStatus.BAD_REQUEST),
	CART_NOT_FOUND("Không tìm thấy giỏ hàng", HttpStatus.NOT_FOUND),
	ITEM_NOT_FOUND_IN_CART("Không tìm thấy sản phẩm trong giỏ hàng", HttpStatus.NOT_FOUND),
	CART_EMPTY("Giỏ hàng đang trống", HttpStatus.BAD_REQUEST),
	ORDER_NOT_FOUND("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND),
	ORDER_CANNOT_CANCEL("Đơn hàng không thể huỷ ở trạng thái hiện tại", HttpStatus.BAD_REQUEST)


	;

	private final String message;
	private final HttpStatus status;

	ExceptionOm(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	/**
	 * Lấy ra dữ liệu string message của Enum
	 */
	public String val() {
		return this.message;
	}
}
