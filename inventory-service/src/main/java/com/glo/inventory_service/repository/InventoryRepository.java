package com.glo.inventory_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glo.inventory_service.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {


	List<Inventory> findBySkuCodeIn(List<String> skuCode);

}
