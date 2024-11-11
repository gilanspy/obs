package com.gilan.test.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gilan.test.model.request.ItemRequest;
import com.gilan.test.model.response.ItemResponse;
import com.gilan.test.persistence.entity.Item;

public interface ItemService {
	ItemResponse saveItem(ItemRequest item);
	ItemResponse getItemById(Long id);
	Item getEntityById(Long id);
    Page<ItemResponse> listItems(Pageable pageable); 
    ItemResponse updateItem(Long id, ItemRequest itemDetails);
    void deleteItem(Long id);
    Integer getRemainingStock(Long id); 
    void updateStockItem (Item item);
}
