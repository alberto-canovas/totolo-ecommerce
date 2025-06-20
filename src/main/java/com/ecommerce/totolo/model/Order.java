package com.ecommerce.totolo.model;

import com.ecommerce.totolo.Enum.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    private LocalDateTime order_date;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String address;
    private Double total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;


    public Order() {
    }

    public Order(Integer id, User user, LocalDateTime order_date, OrderStatus status, String address, Double total, List<OrderItem> orderItems) {
        this.id = id;
        this.user = user;
        this.order_date = order_date;
        this.status = status;
        this.address = address;
        this.total = total;
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", order_date=" + order_date +
                ", total=" + total +
                '}';
    }
}
