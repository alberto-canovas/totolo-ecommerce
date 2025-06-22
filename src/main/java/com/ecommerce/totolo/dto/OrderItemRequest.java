package com.ecommerce.totolo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para cada ítem dentro de la solicitud de creación de un pedido.
 * Contiene el ID del producto y la cantidad.
 */
@Setter
@Getter
public class OrderItemRequest {

    private Long productId;
    private int quantity;

}