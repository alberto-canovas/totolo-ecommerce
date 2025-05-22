package com.ecommerce.totolo.repository;

import com.ecommerce.totolo.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderItemDao extends JpaRepository<OrderItem,Integer> {

}
