package com.service.notificationservice.event;


import com.service.notificationservice.enums.OrderStatus;

public record OrderEvent(String orderNumber, int itemsCount , OrderStatus OrderStatus ) {
}
