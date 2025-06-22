package com.ecommerce.totolo.repository;

import com.ecommerce.totolo.model.Order;
import com.ecommerce.totolo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderDao extends JpaRepository<Order,Integer> {
    List<Order> findByUser(User user);
}
