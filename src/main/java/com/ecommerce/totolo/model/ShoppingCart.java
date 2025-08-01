package com.ecommerce.totolo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="shoppingCart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //cada usuario tiene un solo carrito y cada carrito pertenece a un único usuario
    @OneToOne
    //la tabla carrito tendrá una columna user_id que hará referencia a user.id
    @JoinColumn(name = "user_id")
    @JsonManagedReference // permite la serialización del lado principal
    @JsonIgnore
    private User user;

    //un ShoppingCart puede tener muchos items, cada ShoppingCartItem pertenece a un solo ShoppingCart
    //Cascade se utiliza por si se borra el ShoppingCart se borran automáticamente todos sus ShoppingCartItem
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<ShoppingCartItem> items;




    public ShoppingCart(){}

    public ShoppingCart(User user, List<ShoppingCartItem> items){
        this.user = user;
        this.items = items;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItem> items) {
        this.items = items;
    }

}
