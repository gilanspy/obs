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
import com.gilan.test.model.request.OrderRequest;
import com.gilan.test.model.response.OrderResponse;
import com.gilan.test.persistence.entity.Item;
import com.gilan.test.persistence.repository.ItemRepository;
import com.gilan.test.persistence.repository.OrderRepository;
import com.gilan.test.service.OrderService;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;
    
    @BeforeEach
    public void setup() {
    	orderRepository.deleteAll(); 
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
    public void testSaveOrderWithSufficientStock() {
        Item item = createTestItem("Test Item", 100, 10);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemId(item.getId());
        orderRequest.setQuantity(5);
        orderRequest.setPrice(500);

        OrderResponse savedOrder = orderService.saveOrder(orderRequest);
        assertEquals(5, savedOrder.getQuantity());
        assertEquals(5, itemRepository.findById(item.getId()).get().getStock()); // Stock should be reduced to 5
    }

    @Test
    public void testSaveOrderWithInsufficientStock() {
        Item item = createTestItem("Test Item", 100, 3);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemId(item.getId());
        orderRequest.setQuantity(5); // Exceeds available stock
        orderRequest.setPrice(500);

        AppException exception = assertThrows(AppException.class, () -> {
            orderService.saveOrder(orderRequest);
        });

        assertEquals(ErrorType.INSUFFICIENT_STOCK, exception.getErrorType());
    }

    @Test
    public void testSaveOrderWithNullQuantity() {
        Item item = createTestItem("Test Item", 100, 10);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemId(item.getId());
        orderRequest.setQuantity(null); // null quantity
        orderRequest.setPrice(500);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            orderService.saveOrder(orderRequest);
        });

        String expectedMessage = "Quantity is mandatory";
        assertEquals(expectedMessage, exception.getConstraintViolations().iterator().next().getMessage());
    }

    @Test
    public void testSaveOrderWithNonExistingItem() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemId(999L); // Non-existing item ID
        orderRequest.setQuantity(5);
        orderRequest.setPrice(500);

        AppException exception = assertThrows(AppException.class, () -> {
            orderService.saveOrder(orderRequest);
        });

        assertEquals(ErrorType.ITEM_NOT_FOUND, exception.getErrorType());
    }

    @Test
    public void testGetOrderById() {
        Item item = createTestItem("Test Item", 100, 10);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemId(item.getId());
        orderRequest.setQuantity(3);
        orderRequest.setPrice(300);

        OrderResponse savedOrder = orderService.saveOrder(orderRequest);
        OrderResponse fetchedOrder = orderService.getOrderById(savedOrder.getId());

        assertEquals(savedOrder.getId(), fetchedOrder.getId());
        assertEquals(savedOrder.getQuantity(), fetchedOrder.getQuantity());
    }
}
