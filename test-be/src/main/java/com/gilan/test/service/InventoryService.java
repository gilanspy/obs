package com.gilan.test.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gilan.test.model.request.InventoryRequest;
import com.gilan.test.model.response.InventoryResponse;
import com.gilan.test.persistence.entity.Inventory;

public interface InventoryService {
	InventoryResponse saveInventory(InventoryRequest inventory);

	InventoryResponse getInventoryById(Long id);

	Page<InventoryResponse> listInventories(Pageable pageable);

	InventoryResponse updateInventory(Long id, InventoryRequest inventoryDetails);

	void deleteInventory(Long id);
	
	Inventory getEntityById(Long id);
}
