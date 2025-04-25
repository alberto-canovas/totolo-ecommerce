package com.ecommerce.totolo.repository;

import com.ecommerce.totolo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductDao extends JpaRepository<Product,Integer> {
}
