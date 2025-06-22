package com.ecommerce.totolo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Significa que muchos OrderItem pueden estar asociados al mismo Product.
    @ManyToOne
    private Product product;
    private Integer quantity;
    private double price;

    //Significa que muchos orderItem pertenecen al mismo order.
    @ManyToOne
    @JsonBackReference
    private Order order;


    public OrderItem() {
    }

    public OrderItem(Integer id, Product product, Integer quantity, double price, Order order) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.order = order;
    }


    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
