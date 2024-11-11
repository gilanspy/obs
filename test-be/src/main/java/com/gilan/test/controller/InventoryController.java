package com.gilan.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gilan.test.model.request.InventoryRequest;
import com.gilan.test.model.response.InventoryResponse;
import com.gilan.test.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	@GetMapping
	public Page<InventoryResponse> listInventories(Pageable pageable) {
		return inventoryService.listInventories(pageable);
	}

	@GetMapping("/{id}")
	public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long id) {
		return ResponseEntity.ok(inventoryService.getInventoryById(id));
	}

	@PostMapping
	public InventoryResponse createInventory(@RequestBody InventoryRequest inventory) {
		return inventoryService.saveInventory(inventory);
	}

	@PutMapping("/{id}")
	public InventoryResponse updateInventory(@PathVariable Long id, @RequestBody InventoryRequest inventoryDetails) {
		return inventoryService.updateInventory(id, inventoryDetails);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
		inventoryService.deleteInventory(id);
		return ResponseEntity.ok("inventory berhasil di delete");
	}
}
