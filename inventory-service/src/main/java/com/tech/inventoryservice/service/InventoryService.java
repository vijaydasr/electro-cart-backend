package com.tech.inventoryservice.service;

import com.tech.inventoryservice.model.Inventory;
import com.tech.inventoryservice.record.InventoryRequest;
import com.tech.inventoryservice.record.InventoryResponse;
import com.tech.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity) {
        return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }

    public List<InventoryResponse> getAllInventories() {
        return inventoryRepository.findAll()
                .stream()
                .map(inventory -> new InventoryResponse(inventory.getId(), inventory.getSkuCode(), inventory.getQuantity()))
                .toList();
    }

    public InventoryResponse addInventory(InventoryRequest inventory) {
        Inventory existingItem = inventoryRepository.findBySkuCode(inventory.skuCode());
        if(existingItem != null) {
            existingItem.setQuantity(inventory.quantity());
            inventoryRepository.save(existingItem);
            return new InventoryResponse(existingItem.getId(), existingItem.getSkuCode(), existingItem.getQuantity());
        } else {
            Inventory inventoryEntity = Inventory.builder().skuCode(inventory.skuCode()).quantity(inventory.quantity()).build();
            inventoryRepository.save(inventoryEntity);
        }

        return new InventoryResponse(inventory.id(), inventory.skuCode(), inventory.quantity());
    }
}
