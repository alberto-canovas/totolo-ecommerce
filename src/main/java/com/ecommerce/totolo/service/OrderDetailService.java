package com.ecommerce.totolo.service;

import com.ecommerce.totolo.model.OrderDetail;
import com.ecommerce.totolo.repository.IOrderDetailDao;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {
    @Autowired
    IOrderDetailDao orderDetailDao;

    public OrderDetail newOrderDetail(OrderDetail orderDetail){
        return orderDetailDao.save(orderDetail);
    }

    public List<OrderDetail> getAllOrderDetail(){
        return orderDetailDao.findAll();
    }

    public Optional<OrderDetail> getOrderDetailById(Integer id){
        return orderDetailDao.findById(id);
    }

    public void deleteOrderDetailById(Integer id){
        orderDetailDao.deleteById(id);
    }
}
