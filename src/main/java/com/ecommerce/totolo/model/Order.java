package com.ecommerce.totolo.model;

import java.util.Date;

public class Order {
    private Integer id;
    private String number;
    private Date order_date;

    private double total;

    public Order() {
    }

    public Order(Integer id, String number, Date order_date, double total) {
        this.id = id;
        this.number = number;
        this.order_date = order_date;
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", order_date=" + order_date +
                ", total=" + total +
                '}';
    }
}
