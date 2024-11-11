package com.gilan.test.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gilan.test.exception.AppException;
import com.gilan.test.model.request.ItemRequest;
import com.gilan.test.model.response.ItemResponse;
import com.gilan.test.persistence.entity.Item;

import jakarta.validation.Valid;

public interface ItemService {
	ItemResponse saveItem(@Valid ItemRequest item);
	ItemResponse getItemById(Long id) throws AppException;
	Item getEntityById(Long id);
    Page<ItemResponse> listItems(Pageable pageable); 
    ItemResponse updateItem(Long id, ItemRequest itemDetails);
    void deleteItem(Long id);
    Integer getRemainingStock(Long id); 
    void updateStockItem (Item item);
}
