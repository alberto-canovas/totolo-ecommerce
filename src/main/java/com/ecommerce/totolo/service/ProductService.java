package com.ecommerce.totolo.service;

import com.ecommerce.totolo.model.Product;
import com.ecommerce.totolo.repository.IProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    IProductDao productDao;

    public Product addNewProduct (Product product){
        return productDao.save(product);
    }

    public List<Product> getAllProducts(){
        return productDao.findAll();
    }

    public Optional<Product> getProductById(Integer id){
        return productDao.findById(id);
    }

    public void deleteProductById(Integer id){
        productDao.deleteById(id);
    }
}
