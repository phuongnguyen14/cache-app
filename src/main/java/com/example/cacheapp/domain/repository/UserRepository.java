package com.example.cacheapp.domain.repository;

import com.example.cacheapp.domain.model.User;
import com.example.cacheapp.app.dtos.filter.UserParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByIdAndIsDeletedFalse(Long id);

	boolean existsByUserNameAndIsDeletedFalse(String userName);

	@Query("""
			SELECT u FROM User u 
			WHERE u.isDeleted = false
			AND (
			    :#{#userParam.gender} IS NULL 
			    OR u.gender LIKE concat('%', :#{#userParam.gender}, '%')
			)
			AND (
			    :#{#userParam.state} IS NULL 
			    OR u.userState LIKE concat('%', :#{#userParam.state}, '%')
			)
			AND (
			    :#{#userParam.dateOfBirthFrom} IS NULL 
			    OR :#{#userParam.dateOfBirthTo} IS NULL
			    OR u.dateOfBirth BETWEEN :#{#userParam.dateOfBirthFrom} AND :#{#userParam.dateOfBirthTo}
			)
			AND (
			    :#{#userParam.search} IS NULL 
			    OR lower(u.userName) LIKE concat('%', lower(:#{#userParam.search}), '%')
			)
			""")
	Page<User> filter(UserParam userParam, Pageable pageable);
}
