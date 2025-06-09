package com.gateway.service.productservice.services;


import com.gateway.service.productservice.model.dto.ProductRequest;
import com.gateway.service.productservice.model.dto.ProductResponse;
import com.gateway.service.productservice.model.entities.Product;
import com.gateway.service.productservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public void createProduct(ProductRequest productRequest) {
        var product = Product.builder()
                .sku(productRequest.sku())
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .status(productRequest.status())
                .build();
        productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }
}
