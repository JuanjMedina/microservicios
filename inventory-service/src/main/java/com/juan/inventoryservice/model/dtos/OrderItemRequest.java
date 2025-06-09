package com.juan.inventoryservice.model.dtos;

public record OrderItemRequest(
        Long id,
        String sku,
        Double price,
        Long quantity
) {
    public OrderItemRequest {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be null or blank");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price must be a non-negative value");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive value");
        }
    }


}
