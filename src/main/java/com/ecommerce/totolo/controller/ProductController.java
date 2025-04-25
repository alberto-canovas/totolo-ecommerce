package com.ecommerce.totolo.controller;

import com.ecommerce.totolo.model.Product;
import com.ecommerce.totolo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/totolo/v1")
//Define la URL base para los endpoints de este controlador.
public class ProductController {

    @Autowired
    ProductService productService;

    //OBTENER TODOS LOS PRODUCTOS
    @GetMapping("/products")
    @CrossOrigin
    //permite de manera segura acceder a recursos en otro dominio desde cualquier origen
    public ResponseEntity<List<Product>> getAllProducts(){
        //ResponseEntity se utiliza para proporcionar al cliente una respuesta más completa y controlada.
        //Te da un control total sobre la respuesta HTTP que envías.
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //ELIMINAR UN PRODUCTO POR ID
    @DeleteMapping("/product/{id}")
    @CrossOrigin
    //Con el pathVariable le decimos que el texto "id" de la ruta hace referencia a la variable de tipo Integer id
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Integer id){
        Optional<Product> optionalProduct = productService.getProductById(id);
        //si existe el producto con el id lo guardamos en la variable product
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            productService.deleteProductById(id);
            String message = "Product: " + product.getName() + " deleted successfully";
            return new ResponseEntity<>(message,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Product not found",HttpStatus.NOT_FOUND);
        }
    }

    //CREAR UN NUEVO PRODUCTO
    @PostMapping("/product")
    @CrossOrigin
    public ResponseEntity<String> addNewProduct (@RequestBody Product product){
        try{
            Product savedProduct = productService.addNewProduct(product);
            String message = "Product: "+product.getName()+" saved successfully ";
            return  new ResponseEntity<>(message,HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error saving the product"+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //EDITAR PRODUCTO
    @PutMapping("/product/{id}")
    @CrossOrigin
    public ResponseEntity<String> updateProductById(@PathVariable("id")Integer id, @RequestBody Product product){
        Optional<Product> optionalProduct = productService.getProductById(id);
        if(optionalProduct.isPresent()){
            Product existentProduct = optionalProduct.get();
            existentProduct.setName(product.getName());
            existentProduct.setDescription((product.getDescription()));
            existentProduct.setImage(product.getImage());
            existentProduct.setPrice(product.getPrice());
            existentProduct.setStock(product.getStock());
            productService.addNewProduct(existentProduct);
            return new ResponseEntity<>("Product updated successfully",HttpStatus.OK);
        }else{
            return  new ResponseEntity<>("Product not found",HttpStatus.NOT_FOUND);
        }
    }

    //BUSCAR PRODUCTO POR ID
    @GetMapping("/product/{id}")
    @CrossOrigin
    public ResponseEntity<Product> getProductById(@PathVariable("id") Integer id){
        Optional<Product> product = productService.getProductById(id);
//        if(product.isPresent()){
//            Product existentProduct = product.get();
//            return ResponseEntity.ok(existentProduct);
//        }else{
//            return ResponseEntity.notFound().build();
//        }
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());


    }



}
