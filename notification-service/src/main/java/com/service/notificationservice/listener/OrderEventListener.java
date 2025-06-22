package com.service.notificationservice.listener;

import com.service.notificationservice.event.OrderEvent;
import com.service.notificationservice.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventListener {
    @KafkaListener(topics = "orders-topic")
    public void handleOrderNotifications(String message) {
        var orderEvent = JsonUtils.fromJson(message, OrderEvent.class);

        //send mail or notification logic here

        log.info("Order {} event recived for order {} with items {} ", orderEvent.OrderStatus(), orderEvent.orderNumber(), orderEvent.itemsCount());
    }
}
