package com.ecommerce.totolo.repository;

import com.ecommerce.totolo.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IShoppingCartDao extends JpaRepository<ShoppingCart,Integer> {

}
