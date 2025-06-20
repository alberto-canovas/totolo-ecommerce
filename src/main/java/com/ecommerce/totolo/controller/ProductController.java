package com.ecommerce.totolo.controller;

import com.ecommerce.totolo.model.Product;
import com.ecommerce.totolo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/totolo/v1")
@CrossOrigin // Aplica para todos los métodos
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Integer id) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            try {
                productService.deleteProductById(id);
                String message = "Product: " + optionalProduct.get().getName() + " deleted successfully";
                return new ResponseEntity<>(message, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error deleting product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addNewProduct(@Valid @RequestBody Product product) {
        try {
            Product savedProduct = productService.addNewProduct(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving the product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable("id") Integer id, @Valid @RequestBody Product product) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();

            // Validar antes de setear cada campo para evitar nulidad o vacío si quieres
            if (product.getName() != null && !product.getName().isBlank()) {
                existingProduct.setName(product.getName());
            }
            if (product.getDescription() != null) {
                existingProduct.setDescription(product.getDescription());
            }
            if (product.getImage() != null) {
                existingProduct.setImage(product.getImage());
            }
            if (product.getPrice() != null) {
                existingProduct.setPrice(product.getPrice());
            }
            if (product.getStock() != null) {
                existingProduct.setStock(product.getStock());
            }

            try {
                productService.addNewProduct(existingProduct);
                return new ResponseEntity<>(existingProduct, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error updating product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Integer id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
