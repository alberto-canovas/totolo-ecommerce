package com.ecommerce.totolo.service;

import com.ecommerce.totolo.Enum.OrderStatus;
import com.ecommerce.totolo.dto.CreateOrderRequest;
import com.ecommerce.totolo.dto.OrderItemRequest;
import com.ecommerce.totolo.model.*;
import com.ecommerce.totolo.repository.IOrderDao;
import com.ecommerce.totolo.repository.IProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IProductDao productDao;

    @Autowired
    private ShoppingCartService shoppingCartService;


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


    /**
     * NUEVO MÉTODO: Crea una orden a partir de un payload del frontend.
     * @param user El usuario autenticado que realiza la orden.
     * @param orderRequest El DTO que contiene la dirección y los productos.
     * @return La orden guardada en la base de datos.
     */
    public Order createOrderFromPayload(User user, CreateOrderRequest orderRequest) {
        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new RuntimeException("La solicitud de pedido no contiene productos.");
        }

        // 1. Crear el objeto principal de la orden
        Order order = new Order();
        order.setUser(user);
        order.setAddress(orderRequest.getAddress());
        order.setOrder_date(LocalDateTime.now());
        order.setStatus(OrderStatus.IN_PROCESS); // O el estado inicial que prefieras

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        // 2. Procesar cada producto de la solicitud
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            // Busca el producto en la BBDD para asegurar que existe y obtener el precio real
            Product product = productDao.findById(itemRequest.getProductId().intValue())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + itemRequest.getProductId()));

            // Crea el item de la orden
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice()); // Usa el precio de la BBDD, no del cliente
            orderItem.setOrder(order); // Enlaza el item con la orden principal

            orderItems.add(orderItem);
            total += product.getPrice() * itemRequest.getQuantity();
        }

        // 3. Asignar la lista de items y el total a la orden
        order.setOrderItems(orderItems);
        order.setTotal(total);

        // 4. Guardar la orden y sus items en la BBDD (gracias a CascadeType.ALL)
        return orderDao.save(order);
    }
}