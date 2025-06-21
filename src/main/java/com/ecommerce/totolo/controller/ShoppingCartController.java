package com.ecommerce.totolo.controller;

import com.ecommerce.totolo.model.ShoppingCartItem;
import com.ecommerce.totolo.model.User;
import com.ecommerce.totolo.repository.IUserDao;
import com.ecommerce.totolo.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/totolo/v1/cart")
@CrossOrigin
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private IUserDao userDao;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(Principal principal,
                                       @RequestParam Integer productId,
                                       @RequestParam Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return ResponseEntity.badRequest().body("La cantidad debe ser mayor que 0");
        }
        try {
            String username = principal.getName();
            User user = userDao.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            cartService.addProductToCart(user, productId, quantity);
            return ResponseEntity.ok("Producto agregado al carrito");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al agregar el producto: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getCart(Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            List<ShoppingCartItem> items = cartService.getCartItems(user);
            return ResponseEntity.ok(items);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart(Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            cartService.clearCart(user);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromCart(Principal principal, @RequestParam Integer productId) {
        try {
            String username = principal.getName();
            User user = userDao.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            List<ShoppingCartItem> updatedCart = cartService.removeProductFromCart(user, productId);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }
}
