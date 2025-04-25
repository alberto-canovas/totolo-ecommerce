package com.ecommerce.totolo.repository;

import com.ecommerce.totolo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderDao extends JpaRepository<Order,Integer> {
}
