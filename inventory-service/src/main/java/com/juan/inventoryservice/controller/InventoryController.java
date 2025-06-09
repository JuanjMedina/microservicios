package com.juan.inventoryservice.controller;

import com.juan.inventoryservice.model.dtos.BaseResponse;
import com.juan.inventoryservice.model.dtos.OrderItemRequest;
import com.juan.inventoryservice.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    @GetMapping("/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkInventory(@PathVariable("sku") String sku) {
        return inventoryService.isInStock(sku);
    }

    @PostMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse checkInStock(@RequestBody List<OrderItemRequest> orderItemRequests) {
        log.info(orderItemRequests.toString());
        return inventoryService.areInStock(orderItemRequests);
    }
}
