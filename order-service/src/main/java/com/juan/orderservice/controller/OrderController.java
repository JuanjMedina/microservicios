package com.juan.orderservice.controller;

import com.juan.orderservice.model.dtos.OrderRequest;
import com.juan.orderservice.model.entities.Order;
import com.juan.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "order-service", fallbackMethod = "createOrderFallback")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
        orderService.createOrder(orderRequest); 
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<String> createOrderFallback(OrderRequest orderRequest, Throwable throwable) {
        // Log the error or handle it as needed
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create order due to: " + throwable.getMessage());
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
