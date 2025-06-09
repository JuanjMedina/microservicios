package com.juan.orderservice.model.dtos;

import java.util.List;

public record OrderRequest(
        List<OrderItemRequest> orderItems
) {
    public OrderRequest {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be null or empty");
        }
    }
    public List<OrderItemRequest> orderItems() {
        return orderItems;
    }


}
