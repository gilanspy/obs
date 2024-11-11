package com.gilan.test.test_be;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gilan.test.exception.AppException;
import com.gilan.test.model.ErrorType;
import com.gilan.test.model.request.InventoryRequest;
import com.gilan.test.model.response.InventoryResponse;
import com.gilan.test.persistence.entity.Item;
import com.gilan.test.persistence.repository.InventoryRepository;
import com.gilan.test.persistence.repository.ItemRepository;
import com.gilan.test.service.InventoryService;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InventoryServiceImplTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void setup() {
    	inventoryRepository.deleteAll();
        itemRepository.deleteAll();
    }

    private Item createTestItem(String name, int price, int stock) {
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setStock(stock);
        return itemRepository.save(item);
    }

    @Test
    public void testSaveInventoryWithTopUp() {
        Item item = createTestItem("Test Item", 100, 0);

        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemId(item.getId());
        inventoryRequest.setQuantity(10);
        inventoryRequest.setType("T"); 

        InventoryResponse savedInventory = inventoryService.saveInventory(inventoryRequest);
        assertEquals(10, savedInventory.getQuantity());
        assertEquals(10, itemRepository.findById(item.getId()).get().getStock()); 
    }

    @Test
    public void testSaveInventoryWithInsufficientStock() {
        Item item = createTestItem("Test Item", 100, 5);

        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemId(item.getId());
        inventoryRequest.setQuantity(10); 
        inventoryRequest.setType("W"); 

        AppException exception = assertThrows(AppException.class, () -> {
            inventoryService.saveInventory(inventoryRequest);
        });
        
        assertEquals(ErrorType.INSUFFICIENT_STOCK, exception.getErrorType());
    }

    @Test
    public void testSaveInventoryWithNullQuantity() {
        Item item = createTestItem("Test Item", 100, 10);

        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemId(item.getId());
        inventoryRequest.setQuantity(null);
        inventoryRequest.setType("T");

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            inventoryService.saveInventory(inventoryRequest);
        });
        
        exception.getConstraintViolations().forEach(violation -> 
        System.out.println("Violation: " + violation.getMessage())
    );
        System.out.println("a "+exception.getConstraintViolations().iterator().next().getMessage());
        String expectedMessage = "Quantity is mandatory";
        
        
        assertEquals(expectedMessage, exception.getConstraintViolations().iterator().next().getMessage());
    }

    @Test
    public void testSaveInventoryWithNullType() {
        Item item = createTestItem("Test Item", 100, 10);

        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemId(item.getId());
        inventoryRequest.setQuantity(5);
        inventoryRequest.setType(null); 

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            inventoryService.saveInventory(inventoryRequest);
        });
        
        String expectedMessage = "Type is mandatory";
        assertEquals(expectedMessage, exception.getConstraintViolations().iterator().next().getMessage());
    }

    @Test
    public void testSaveInventoryWithNonExistingItem() {
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemId(999L); 
        inventoryRequest.setQuantity(5);
        inventoryRequest.setType("T");

        AppException exception = assertThrows(AppException.class, () -> {
            inventoryService.saveInventory(inventoryRequest);
        });

        assertEquals(ErrorType.ITEM_NOT_FOUND, exception.getErrorType());
    }
}

