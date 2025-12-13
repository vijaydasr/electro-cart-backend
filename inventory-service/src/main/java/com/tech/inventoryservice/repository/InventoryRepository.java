package com.tech.inventoryservice.repository;

import com.tech.inventoryservice.model.Inventory;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skuCode, Integer quantity);

    Inventory findBySkuCode(String skuCode);
}
