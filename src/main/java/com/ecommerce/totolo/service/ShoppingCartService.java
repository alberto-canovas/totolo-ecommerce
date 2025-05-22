package com.ecommerce.totolo.service;

import com.ecommerce.totolo.model.ShoppingCart;
import com.ecommerce.totolo.repository.IShoppingCartDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {
    @Autowired
    IShoppingCartDao shoppingCartDao;

    public ShoppingCart addNewShoppingCart (ShoppingCart shoppingCart){
        return shoppingCartDao.save(shoppingCart);
    }

    public List<ShoppingCart> getAllShoppingCarts(){
        return shoppingCartDao.findAll();
    }

    //uso de Optional para que no nos devuelva null si no existe el usuario con id

    public Optional<ShoppingCart> getShoppingCartById(Integer id){
        return shoppingCartDao.findById(id);
    }

    public void deleteShoppingCartById(Integer id){
        shoppingCartDao.deleteById(id);
    }
}
