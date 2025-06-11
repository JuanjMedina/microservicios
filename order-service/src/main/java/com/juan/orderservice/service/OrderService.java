package com.juan.orderservice.service;

import com.juan.orderservice.model.dtos.BaseResponse;
import com.juan.orderservice.model.dtos.OrderItemRequest;
import com.juan.orderservice.model.dtos.OrderRequest;
import com.juan.orderservice.model.entities.Order;
import com.juan.orderservice.model.entities.OrderItems;
import com.juan.orderservice.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public OrderService(OrderRepository orderRepository, WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
    }


    public void createOrder(OrderRequest orderRequest) {
        BaseResponse result = this.webClientBuilder.build()
                .post()
                .uri("lb://inventory-service/api/v1/inventory/in-stock")
                .bodyValue(orderRequest.orderItems())  // Enviamos solo la lista de OrderItemRequest
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .block();

        if (result != null && result.hasErrors()) {
            throw new RuntimeException("Error fetching products: " + String.join(", ", result.errorMessages()));
        }

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderItems(orderRequest.orderItems().stream()
                .map(orderItemRequest -> mapOrderRequestToOrderItems(orderItemRequest, order)
                ).toList());

        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    private OrderItems mapOrderRequestToOrderItems(OrderItemRequest orderItemRequest, Order order) {
        return OrderItems.builder()
                .sku(orderItemRequest.sku())
                .price(orderItemRequest.price())
                .quantity(orderItemRequest.quantity())
                .order(order)
                .build();
    }


}
