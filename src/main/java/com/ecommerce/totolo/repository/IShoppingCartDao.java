package com.ecommerce.totolo.repository;

import com.ecommerce.totolo.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IShoppingCartDao extends JpaRepository<ShoppingCart, Integer> {
    Optional<ShoppingCart> findByUserId(Integer userId);
}
  