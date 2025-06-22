package com.ecommerce.totolo.service;

import com.ecommerce.totolo.Enum.OrderStatus;
import com.ecommerce.totolo.model.*;
import com.ecommerce.totolo.repository.IOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public Order getOrderById(Integer id) {
        return orderDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    public List<Order> getOrdersByUser(User user) {
        return orderDao.findByUser(user);
    }

    public void deleteOrderById(Integer id){
        orderDao.deleteById(id);
    }

    public Order updateOrderStatus(Integer orderId, String newStatus) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + orderId));

        try {
            OrderStatus statusEnum = OrderStatus.valueOf(newStatus.toUpperCase());
            order.setStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido. Usa: IN_PROCESS, SENT o CANCELLED");
        }

        return orderDao.save(order);
    }


    @Autowired
    private ShoppingCartService shoppingCartService;

    public Order createOrderFromCart(User user, String address) {
        ShoppingCart cart = shoppingCartService.getOrCreateCart(user);
        List<ShoppingCartItem> items = cart.getItems();

        if (items == null || items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setOrder_date(LocalDateTime.now());
        order.setStatus(OrderStatus.IN_PROCESS);

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (ShoppingCartItem cartItem : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }

        order.setOrderItems(orderItems);
        order.setTotal(total);

        Order savedOrder = orderDao.save(order);

        // Limpia el carrito después de crear la orden
        shoppingCartService.clearCart(user);

        return savedOrder;
    }





}
