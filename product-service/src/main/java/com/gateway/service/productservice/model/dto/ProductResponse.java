package com.gateway.service.productservice.model.dto;

import com.gateway.service.productservice.model.entities.Product;
import lombok.Builder;
import lombok.Data;

@Builder
public record ProductResponse(String sku,
                              String name,
                              String description,
                              Double price,
                              Boolean status) {

    public static ProductResponse from(Product productRequest) {
        return ProductResponse.builder()
                .sku(productRequest.getSku())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .status(productRequest.getStatus())
                .build();
    }

}
