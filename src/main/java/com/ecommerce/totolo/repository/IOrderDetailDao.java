package com.ecommerce.totolo.repository;

import com.ecommerce.totolo.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderDetailDao extends JpaRepository<OrderDetail,Integer> {

}
