package com.ecommerce.totolo.service;

import com.ecommerce.totolo.model.Order;
import com.ecommerce.totolo.repository.IOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    IOrderDao orderDao;

    public Order addNewOrder(Order order){
        return orderDao.save(order);
    }

    public List<Order> getAllOrders(){
        return orderDao.findAll();
    }

    public Optional<Order> getOrderById (Integer id){
        return orderDao.findById(id);
    }

    public void deleteOrderById(Integer id){
        orderDao.deleteById(id);
    }

}
