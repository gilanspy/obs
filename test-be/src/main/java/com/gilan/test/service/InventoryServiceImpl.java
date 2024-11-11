package com.gilan.test.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.gilan.test.exception.AppException;
import com.gilan.test.model.ErrorType;
import com.gilan.test.model.request.InventoryRequest;
import com.gilan.test.model.response.InventoryResponse;
import com.gilan.test.persistence.entity.Inventory;
import com.gilan.test.persistence.entity.Inventory.Type;
import com.gilan.test.persistence.entity.Item;
import com.gilan.test.persistence.repository.InventoryRepository;

@Service
@Validated
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public InventoryResponse saveInventory(InventoryRequest inventoryRequest) {
    	
    	  if (!Type.isValidType(inventoryRequest.getType())) {
    	        throw new AppException("Type must be either 'T' (Top Up) or 'W' (Withdraw)", ErrorType.INVALID_TYPE);
    	    }
    	
        Item item = itemService.getEntityById(inventoryRequest.getItemId());

        Inventory inventory = new Inventory();
        inventory.setItem(item);
        inventory.setQuantity(inventoryRequest.getQuantity());
        inventory.setType(inventoryRequest.getType().equals("T") ? Inventory.Type.T : Inventory.Type.W);

        if (inventory.getType() == Inventory.Type.T) {
            item.setStock(item.getStock() + inventory.getQuantity());
        } else if (inventory.getType() == Inventory.Type.W) {
            if (item.getStock() < inventory.getQuantity()) {
                throw new AppException("Stok tidak mencukupi untuk withdrawal", ErrorType.INSUFFICIENT_STOCK);
            }
            item.setStock(item.getStock() - inventory.getQuantity());
        }

        itemService.updateStockItem(item);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return convertToResponse(savedInventory);
    }

    @Override
    public InventoryResponse getInventoryById(Long id) {
        Inventory inventory =  getEntityById(id);
        return convertToResponse(inventory);
    }

    @Override
    public Page<InventoryResponse> listInventories(Pageable pageable) {
        Page<Inventory> inventories = inventoryRepository.findAll(pageable);
        return new PageImpl<>(
                inventories.getContent().stream().map(this::convertToResponse).collect(Collectors.toList()),
                pageable,
                inventories.getTotalElements()
        );
    }

    @Override
    public InventoryResponse updateInventory(Long id, InventoryRequest inventoryRequest) {
        Inventory inventory =  getEntityById(id);

        inventory.setQuantity(inventoryRequest.getQuantity());
        inventory.setType(inventoryRequest.getType().equals("T") ? Inventory.Type.T : Inventory.Type.W);

        Item item = inventory.getItem();
        if (inventory.getType() == Inventory.Type.T) {
            item.setStock(item.getStock() + inventory.getQuantity());
        } else if (inventory.getType() == Inventory.Type.W) {
            if (item.getStock() < inventory.getQuantity()) {
                throw new AppException("Stok tidak mencukupi untuk withdrawal", ErrorType.INSUFFICIENT_STOCK);
            }
            item.setStock(item.getStock() - inventory.getQuantity());
        }
        itemService.updateStockItem(item);
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return convertToResponse(updatedInventory);
    }

    @Override
    public void deleteInventory(Long id) {
        Inventory inventory = getEntityById(id);
        inventoryRepository.delete(inventory);
    }

    private InventoryResponse convertToResponse(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        response.setItemId(inventory.getItem().getId());
        response.setQuantity(inventory.getQuantity());
        response.setType(inventory.getType().name());
        return response;
    }

	@Override
	public Inventory getEntityById(Long id) {
		return inventoryRepository.findById(id)
                .orElseThrow(() -> new AppException("inventory tidak ditemukan dengan ID: " + id, ErrorType.INVENTORY_NOT_FOUND));
	}
}
