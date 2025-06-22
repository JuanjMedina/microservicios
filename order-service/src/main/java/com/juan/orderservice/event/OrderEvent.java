package com.juan.orderservice.event;

import com.juan.orderservice.enums.OrderStatus;

public record OrderEvent(String orderNumber, int itemsCount , OrderStatus OrderStatus ) {
}
