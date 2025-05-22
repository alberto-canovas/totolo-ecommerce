package com.ecommerce.totolo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shopping_cart_items")
public class ShoppingCartItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Product product;

    private Integer quantity;

    @ManyToOne
    private ShoppingCart shoppingCart;


}
