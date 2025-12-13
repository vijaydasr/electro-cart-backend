package com.tech.inventoryservice.record;

public record InventoryRequest(Long id, String skuCode, Integer quantity) {
}
