package com.juan.orderservice.service;

import com.juan.orderservice.enums.OrderStatus;
import com.juan.orderservice.event.OrderEvent;
import com.juan.orderservice.model.dtos.BaseResponse;
import com.juan.orderservice.model.dtos.OrderItemRequest;
import com.juan.orderservice.model.dtos.OrderRequest;
import com.juan.orderservice.model.entities.Order;
import com.juan.orderservice.model.entities.OrderItems;
import com.juan.orderservice.repositories.OrderRepository;
import com.juan.orderservice.utils.JsonUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@lombok.extern.slf4j.Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderService(OrderRepository orderRepository, WebClient.Builder webClientBuilder, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
        this.kafkaTemplate = kafkaTemplate;
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

        var saveOrder = orderRepository.save(order);
        log.info("Order created with ID: {}", saveOrder.getId());



        // Enviar mensaje a Kafka con manejo de completado/error
        String orderJson = JsonUtils.toJson(
                new OrderEvent(saveOrder.getOrderNumber(),
                        saveOrder.getOrderItems().size(),
                        OrderStatus.COMPLETED
                )
        );

        this.kafkaTemplate.send("orders-topic", orderJson);

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
