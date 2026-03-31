package com.example.cacheapp.domain.repository;

import com.example.cacheapp.domain.model.Cart;
import com.example.cacheapp.domain.model.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByUserIdAndStatusIn(Long userId, Collection<CartStatus> statuses);

}
