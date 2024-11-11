package com.gilan.test.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse{
    private Long id;
    private Long itemId;
    private Integer quantity;
    private String type;

}
