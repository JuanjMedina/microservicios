package com.gateway.service.productservice.model.dto;

public record ProductRequest(String sku, String name, String description, Double price, Boolean status) {

    public ProductRequest {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price must be a non-negative value");
        }
    }
}
