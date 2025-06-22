package com.ecommerce.totolo.controller;

import com.ecommerce.totolo.Enum.TypeEnum;
import com.ecommerce.totolo.model.Order;
import com.ecommerce.totolo.model.User;
import com.ecommerce.totolo.service.OrderService;
import com.ecommerce.totolo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/totolo/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(Principal principal) {
        Optional<User> optionalUser = userService.findByUsername(principal.getName());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).build(); // Usuario no encontrado o no autenticado
        }
        User user = optionalUser.get();
        List<Order> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer id, Principal principal) {
        Optional<User> optionalUser = userService.findByUsername(principal.getName());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).build(); // Usuario no encontrado o no autenticado
        }
        User user = optionalUser.get();

        try {
            Order order = orderService.getOrderById(id);

            if (!order.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(403).body("No tienes permiso para ver esta orden");
            }

            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer id,
                                               @RequestParam String status,
                                               Principal principal) {
        Optional<User> optionalUser = userService.findByUsername(principal.getName());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }
        User user = optionalUser.get();

        if (user.getType() != TypeEnum.ADMIN) {
            return ResponseEntity.status(403).body("Acceso denegado: Solo los administradores pueden cambiar el estado de una orden");
        }

        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order orderRequest, Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        try {
            Order createdOrder = orderService.createOrderFromCart(user, orderRequest.getAddress());
            return ResponseEntity.ok(createdOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
