package com.ecommerce.totolo.controller;

import com.ecommerce.totolo.model.Product;
import com.ecommerce.totolo.model.ShoppingCart;
import com.ecommerce.totolo.model.ShoppingCartItem;
import com.ecommerce.totolo.service.ShoppingCartService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/totolo/v1/payment")
@CrossOrigin // permite llamadas desde tu frontend Angular
public class PaymentController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    static {
        Stripe.apiKey = "sk_test_51RZ7PHINVtuUnxBwe8f4vLxItqoATRt1VBbIKgQwYBYv97OFXRp1HmSJxdE6ZxMQpC9dd4hElvsLpKR1uUHqysrh00cydebjDp"; // ⚠️ Reemplaza con tu clave secreta real
    }

    @GetMapping("/checkout/{userId}")
    public ResponseEntity<Map<String, String>> createCheckoutSessionFromCart(@PathVariable Integer userId) {
        Optional<ShoppingCart> optionalCart = shoppingCartService.getShoppingCartById(userId);

        if (optionalCart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No cart found for user"));
        }

        ShoppingCart cart = optionalCart.get();

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (ShoppingCartItem item : cart.getItems()) {
            Product product = item.getProduct();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(Long.valueOf(item.getQuantity()))
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("eur")
                                    .setUnitAmount((long) (product.getPrice() * 100)) // Stripe usa céntimos
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(product.getName())
                                                    .setDescription(product.getDescription())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            lineItems.add(lineItem);
        }

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:4200/success")
                    .setCancelUrl("http://localhost:4200/cancel")
                    .addAllLineItem(lineItems)
                    .build();

            Session session = Session.create(params);

            return ResponseEntity.ok(Map.of("id", session.getId()));

        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Stripe error"));
        }
    }
}

