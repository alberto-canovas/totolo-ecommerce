package com.ecommerce.totolo.service;

import com.ecommerce.totolo.model.Product;
import com.ecommerce.totolo.model.ShoppingCart;
import com.ecommerce.totolo.model.ShoppingCartItem;
import com.ecommerce.totolo.model.User;
import com.ecommerce.totolo.repository.IProductDao;
import com.ecommerce.totolo.repository.IShoppingCartDao;
import com.ecommerce.totolo.repository.IShoppingCartItemDao;
import com.ecommerce.totolo.repository.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    @Autowired
    private IShoppingCartDao shoppingCartDao;

    @Autowired
    private IShoppingCartItemDao shoppingCartItemDao;

    @Autowired
    private IProductDao productDao;

    @Autowired
    private IUserDao userDao;

    public ShoppingCart getOrCreateCart(User user) {
        ShoppingCart cart = user.getShoppingCart();
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setUser(user);
            shoppingCartDao.save(cart);
            user.setShoppingCart(cart);
            userDao.save(user);
        }
        return cart;
    }

    public void addProductToCart(User user, Integer productId, Integer quantity) {
        ShoppingCart cart = getOrCreateCart(user);

        Product product = productDao.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        List<ShoppingCartItem> items = cart.getItems();

        if (items == null) {
            items = new ArrayList<>();
            cart.setItems(items);
        }

        // Buscar si el producto ya está en el carrito
        ShoppingCartItem existingItem = items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            shoppingCartItemDao.save(existingItem);
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setShoppingCart(cart);
            shoppingCartItemDao.save(newItem);
            items.add(newItem);
        }

        shoppingCartDao.save(cart);
    }

    public List<ShoppingCartItem> getCartItems(User user) {
        ShoppingCart cart = user.getShoppingCart();
        if (cart == null || cart.getItems() == null) {
            return Collections.emptyList();
        }
        return cart.getItems();
    }

    // Metodo para limpiar carrito tras comprar

    public void clearCart(User user) {
        ShoppingCart cart = user.getShoppingCart();
        if (cart != null && cart.getItems() != null) {
            shoppingCartItemDao.deleteAll(cart.getItems()); //borra en la bd
            cart.getItems().clear(); //limpia la lista en memoria
            shoppingCartDao.save(cart); //guarda el carrito limpio
        }
    }

    public List<ShoppingCartItem> removeProductFromCart(User user, Integer productId) {
        ShoppingCart cart = user.getShoppingCart();
        if (cart != null && cart.getItems() != null) {
            ShoppingCartItem itemToRemove = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (itemToRemove != null) {
                cart.getItems().remove(itemToRemove);
                shoppingCartItemDao.delete(itemToRemove); // Elimina de la base de datos
                shoppingCartDao.save(cart); // Actualiza el carrito
            }
        }

        return cart != null ? cart.getItems() : List.of();
    }

}
