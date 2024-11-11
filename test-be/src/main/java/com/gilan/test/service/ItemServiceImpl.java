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
import com.gilan.test.model.request.ItemRequest;
import com.gilan.test.model.response.ItemResponse;
import com.gilan.test.persistence.entity.Item;
import com.gilan.test.persistence.repository.ItemRepository;

@Service
@Validated
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    
    @Override
    public ItemResponse saveItem(ItemRequest itemRequest){

    	 Item item = new Item();
         item.setName(itemRequest.getName());
         item.setPrice(itemRequest.getPrice());
         item.setStock(0); 
         itemRepository.save(item);
         return mapToResponse(item);
    }

    @Override
    public ItemResponse getItemById(Long id) {
    	Item item = itemRepository.findById(id)
                .orElseThrow(() -> new AppException("Item tidak ditemukan dengan ID: " + id, ErrorType.ITEM_NOT_FOUND));
        
    	return mapToResponse(item);
    }
    

	@Override
	public Item getEntityById(Long id) {
		return itemRepository.findById(id)
                .orElseThrow(() -> new AppException("Item tidak ditemukan dengan ID: " + id, ErrorType.ITEM_NOT_FOUND));
	}

    @Override
    public Page<ItemResponse> listItems(Pageable pageable) {
        Page<Item> items = itemRepository.findAll(pageable);
        return new PageImpl<>(
                items.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()),
                pageable,
                items.getTotalElements()
        );
    }

    @Override
    public ItemResponse updateItem(Long id, ItemRequest itemDetails) {
    	Item item = getEntityById(id);
        item.setName(itemDetails.getName());
        item.setPrice(itemDetails.getPrice());
        itemRepository.save(item);
        return mapToResponse(item);
    }

    @Override
    public void deleteItem(Long id) {
        Item item = getEntityById(id);
        itemRepository.delete(item);
    }

    @Override
    public Integer getRemainingStock(Long id) {
        Item item = getEntityById(id);
        return item.getStock();
    }
    
    private ItemResponse mapToResponse(Item item) {
    	ItemResponse itemDTO = new ItemResponse();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setStock(item.getStock());
        return itemDTO;
    }

	@Override
	public void updateStockItem(Item item) {
		  itemRepository.save(item);
		
	}

}

