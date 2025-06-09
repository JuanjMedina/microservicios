package com.juan.inventoryservice.utils;

import com.juan.inventoryservice.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando lista de inventoryes");
        if(inventoryRepository.findAll().isEmpty()){
            log.info("No hay inventoryes, creando algunos...");
            inventoryRepository.saveAll(
                java.util.List.of(
                    com.juan.inventoryservice.model.entities.Inventory.builder().skuCode("iphone_14").quantity(100L).build(),
                    com.juan.inventoryservice.model.entities.Inventory.builder().skuCode("iphone_14_pro").quantity(100L).build(),
                    com.juan.inventoryservice.model.entities.Inventory.builder().skuCode("iphone_14_pro_max").quantity(100L).build(),
                    com.juan.inventoryservice.model.entities.Inventory.builder().skuCode("iphone_15").quantity(0L).build(),
                    com.juan.inventoryservice.model.entities.Inventory.builder().skuCode("iphone_15_pro").quantity(30L).build(),
                    com.juan.inventoryservice.model.entities.Inventory.builder().skuCode("iphone_15_pro_max").quantity(10L).build()
                )
            );
        }

    }
}
