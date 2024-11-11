package com.gilan.test.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
	@NotNull(message = "ItemId is mandatory")
    private Long itemId;
	
    @NotNull(message = "Quantity is mandatory")
    private Integer quantity;
    
    @NotNull(message = "Type is mandatory")
    private String type; 

}
