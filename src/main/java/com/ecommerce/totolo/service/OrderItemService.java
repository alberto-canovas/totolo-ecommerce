package com.ecommerce.totolo.service;

import com.ecommerce.totolo.model.OrderItem;
import com.ecommerce.totolo.repository.IOrderItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {
    @Autowired
    IOrderItemDao orderItemDao;

    public OrderItem newOrderItem(OrderItem orderItem){
        return orderItemDao.save(orderItem);
    }

    public List<OrderItem> getAllOrderItem(){
        return orderItemDao.findAll();
    }

    public Optional<OrderItem> getOrderItemById(Integer id){
        return orderItemDao.findById(id);
    }

    public void deleteOrderItemById(Integer id){
        orderItemDao.deleteById(id);
    }
}
