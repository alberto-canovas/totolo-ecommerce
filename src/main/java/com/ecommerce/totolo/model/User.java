package com.ecommerce.totolo.model;

import com.ecommerce.totolo.Enum.TypeEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //id autoincrementable
    private String name;
    private String lastname;
    private String username;
    private String email;
    private String address;
    private String phone_number;
    private String password;

    @Enumerated(EnumType.STRING)
    private TypeEnum type;

    //un usuario tiene muchos pedidos
    @OneToMany(mappedBy = "user")
    @JsonBackReference // evita la serialización infinita
    @JsonIgnore
    private List<Order> orderList;

    //un carrito pertenece a un Usuario
    @OneToOne(mappedBy = "user")
    private ShoppingCart shoppingCart;

    public User(){

    }


    public User(Integer id, String name, String lastname, String username, String email, String address, String phone_number, String password, TypeEnum type, List<Order> orderList, ShoppingCart shoppingCart) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone_number = phone_number;
        this.password = password;
        this.type = TypeEnum.CLIENT;
        this.orderList = orderList;
        this.shoppingCart = shoppingCart;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", type='" + type + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
