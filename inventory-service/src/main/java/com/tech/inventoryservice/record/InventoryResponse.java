package com.tech.inventoryservice.record;

public record InventoryResponse(Long id, String skuCode, Integer quantity) {
}
