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
public class OrderRequest {
    private Long itemId;

    @NotNull(message = "Quantity is mandatory")
    private Integer quantity;
    @NotNull(message = "Price is mandatory")
    private Integer price;

}