package com.juan.inventoryservice.service;

import com.juan.inventoryservice.model.dtos.BaseResponse;
import com.juan.inventoryservice.model.dtos.OrderItemRequest;
import com.juan.inventoryservice.model.entities.Inventory;
import com.juan.inventoryservice.repositories.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public boolean isInStock(String sku) {
        var inventory = inventoryRepository.findBySkuCode(sku);
        return inventory.filter(value -> value.getQuantity() > 0).isPresent();
    }

    public BaseResponse areInStock(List<OrderItemRequest> orderItemRequests) {
        var errorList = new ArrayList<String>();

        List<String> skus = orderItemRequests.stream().map(OrderItemRequest::sku).toList();
        List<Inventory> inventoryList = inventoryRepository.findAllBySkuCodeIn(skus);

        orderItemRequests.forEach(orderItemRequest -> {
            var inventory = inventoryList.stream()
                    .filter(inv -> inv.getSkuCode().equals(orderItemRequest.sku()))
                    .findFirst();

            if (inventory.isEmpty() || inventory.get().getQuantity() < orderItemRequest.quantity()) {
                errorList.add("SKU " + orderItemRequest.sku() + " is not in stock or insufficient quantity.");
            }
        });

        return errorList.size() > 0 ? new BaseResponse(errorList.toArray(new String[0])) : new BaseResponse(null);

    }
}
