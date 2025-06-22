package com.ecommerce.totolo.controller;

import com.ecommerce.totolo.Enum.TypeEnum;
import com.ecommerce.totolo.dto.CreateOrderRequest;
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
@CrossOrigin
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

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders(Principal principal, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            User user = null;

            // Intentar obtener usuario del Principal (Spring Security)
            if (principal != null) {
                Optional<User> optionalUser = userService.findByUsername(principal.getName());
                if (optionalUser.isPresent()) {
                    user = optionalUser.get();
                }
            }

            // Si no hay usuario del Principal, intentar obtener del header Authorization
            if (user == null && authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // Remover "Bearer "
                // Aquí podrías implementar lógica para validar el token
                // Por ahora, asumimos que el token es el username
                Optional<User> optionalUser = userService.findByUsername(token);
                if (optionalUser.isPresent()) {
                    user = optionalUser.get();
                }
            }

            if (user == null) {
                return ResponseEntity.status(401).build();
            }

            if (user.getType() != TypeEnum.ADMIN) {
                return ResponseEntity.status(403).build();
            }

            List<Order> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            System.err.println("Error en getAllOrders: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
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

            // Permitir a los ADMINS ver cualquier orden
            if (user.getType() != TypeEnum.ADMIN && !order.getUser().getId().equals(user.getId())) {
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

    /**
     * Crea una nueva orden a partir de los datos enviados desde el frontend.
     * @param orderRequest El payload que contiene la dirección y la lista de productos.
     * @param principal El usuario autenticado.
     * @return La orden creada.
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest orderRequest, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el nombre: " + principal.getName()));

        try {
            // Llamamos a un NUEVO método en el servicio que procesará el payload
            Order createdOrder = orderService.createOrderFromPayload(user, orderRequest);
            return ResponseEntity.ok(createdOrder);
        } catch (RuntimeException e) {
            // Devuelve un mensaje de error más específico
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}