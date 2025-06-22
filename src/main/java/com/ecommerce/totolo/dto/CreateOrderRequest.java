package com.ecommerce.totolo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO (Data Transfer Object) para la creación de un pedido.
 * Contiene la dirección de envío y la lista de productos del pedido.
 */
@Setter
@Getter
public class CreateOrderRequest {


    private String address;
    private List<OrderItemRequest> items;


}