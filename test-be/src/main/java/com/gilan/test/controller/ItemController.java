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

import com.gilan.test.model.request.ItemRequest;
import com.gilan.test.model.response.ItemResponse;
import com.gilan.test.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public Page<ItemResponse> listItems(Pageable pageable) {
        return itemService.listItems(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemRequest item){
        return ResponseEntity.ok(itemService.saveItem(item));
    }

    @PutMapping("/{id}")
    public ItemResponse updateItem(@PathVariable Long id,@Valid  @RequestBody ItemRequest itemDetails) {
        return itemService.updateItem(id, itemDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok("item berhasil di delete");
    }
}
