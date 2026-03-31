package com.example.cacheapp.app.controller;

import com.example.cacheapp.app.dtos.ChangeStateDto;
import com.example.cacheapp.app.dtos.UserDto;
import com.example.cacheapp.app.dtos.filter.UserParam;
import com.example.cacheapp.app.response.UserResponse;
import com.example.cacheapp.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<UserResponse> create(@RequestBody UserDto userDto) {
		return ResponseEntity.ok(userService.create(userDto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> update(
			@PathVariable Long id,
			@RequestBody UserDto userDto
	) {
		return ResponseEntity.ok(userService.update(id, userDto));
	}

	@PatchMapping("/{id}/change-state")
	public ResponseEntity<UserResponse> changeState(
			@PathVariable Long id,
			@RequestBody ChangeStateDto dto
	) {
		return ResponseEntity.ok(userService.changeState(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> get(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getById(id));
	}

	@GetMapping
	public ResponseEntity<Page<UserResponse>> getAll(
			UserParam params,
			@SortDefault(sort = "id", direction = Sort.Direction.DESC)
			Pageable pageable
	) {
		return ResponseEntity.ok(userService.getAll(params, pageable));
	}
}
