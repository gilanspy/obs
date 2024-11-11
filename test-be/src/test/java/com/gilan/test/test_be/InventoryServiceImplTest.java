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
import com.gilan.test.persistence.repository.ItemRepository;
import com.gilan.test.service.InventoryService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InventoryServiceImplTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void setup() {
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
}

