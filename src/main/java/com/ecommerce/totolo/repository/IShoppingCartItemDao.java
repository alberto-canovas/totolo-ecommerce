package com.ecommerce.totolo.repository;

import com.ecommerce.totolo.model.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IShoppingCartItemDao extends JpaRepository<ShoppingCartItem,Integer> {
}
