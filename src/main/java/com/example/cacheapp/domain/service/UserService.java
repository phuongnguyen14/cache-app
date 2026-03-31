package com.example.cacheapp.domain.service;

import com.example.cacheapp.domain.exceptions.CustomException;
import com.example.cacheapp.domain.exceptions.ExceptionOm;
import com.example.cacheapp.domain.model.User;
import com.example.cacheapp.app.dtos.ChangeStateDto;
import com.example.cacheapp.app.dtos.UserDto;
import com.example.cacheapp.app.dtos.filter.UserParam;
import com.example.cacheapp.app.response.UserResponse;
import com.example.cacheapp.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	@Transactional
	public UserResponse create(UserDto userDto) {
		validDto(userDto);
		User user = new User();
		user.setUserName(userDto.getUsername());
		user.setDateOfBirth(userDto.getDateOfBirth());
		user.setGender(userDto.getGender());
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		User savedUser = userRepository.save(user);
		return mapToResponse(savedUser);

	}

	@CacheEvict(value = "users", key = "#id")
	@Transactional
	public UserResponse update(Long id, UserDto userDto) {
		User user = userRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new CustomException(ExceptionOm.USER_NOT_FOUND));
		user.setUserName(userDto.getUsername());
		user.setDateOfBirth(userDto.getDateOfBirth());
		user.setGender(userDto.getGender());
		User savedUser = userRepository.save(user);
		return mapToResponse(savedUser);

	}
	@CacheEvict(value = "users", key = "#id")
	@Transactional
	public UserResponse changeState(Long id, ChangeStateDto dto) {
		User user = userRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new CustomException(ExceptionOm.USER_NOT_FOUND));
		user.setUserState(dto.getState());
		user.setUpdatedAt(LocalDateTime.now());
		User savedUser = userRepository.save(user);
		return mapToResponse(savedUser);
	}
	@CacheEvict(value = "users", key = "#id")
	@Transactional
	public void delete(Long id) {
		User user = userRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new CustomException(ExceptionOm.USER_NOT_FOUND));
		user.setIsDeleted(true);
		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);
	}

	@Cacheable(value = "users", key = "#id")
	public UserResponse getById(Long id) {
		User user = userRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new CustomException(ExceptionOm.USER_NOT_FOUND));
		return mapToResponse(user);
	}

	public Page<UserResponse> getAll(UserParam param, Pageable pageable) {
		Page<User> users = userRepository.filter(param, pageable);
		return users.map(this::mapToResponse);
	}

	public void validDto(UserDto userDto) {
		if (userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
			throw new CustomException(ExceptionOm.USERNAME_IS_REQUIRED);
		}
		boolean exists = userRepository.existsByUserNameAndIsDeletedFalse(userDto.getUsername());
		if (exists) {
			throw new CustomException(ExceptionOm.USERNAME_ALREADY_EXISTS);
		}


	}
	public UserResponse mapToResponse(User user) {
		return UserResponse.builder()
				.id(user.getId())
				.username(user.getUserName())
				.dateOfBirth(user.getDateOfBirth())
				.gender(user.getGender())
				.state(user.getUserState())
				.isDeleted(user.getIsDeleted())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.build();
	}
}
