package com.ecommerce.totolo.service;

import com.ecommerce.totolo.model.ShoppingCartItem;
import com.ecommerce.totolo.repository.IShoppingCartItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartItemService {
    @Autowired
    IShoppingCartItemDao shoppingCartItemDao;

    public ShoppingCartItem addNewShoppingCartItem (ShoppingCartItem shoppingCartItem){
        return shoppingCartItemDao.save(shoppingCartItem);
    }

    public List<ShoppingCartItem> getAllShoppingCartItems(){
        return shoppingCartItemDao.findAll();
    }

    //uso de Optional para que no nos devuelva null si no existe el usuario con id

    public Optional<ShoppingCartItem> getShoppingCartItemById(Integer id){
        return shoppingCartItemDao.findById(id);
    }

    public void deleteShoppingCartItemById(Integer id){
        shoppingCartItemDao.deleteById(id);
    }
}
