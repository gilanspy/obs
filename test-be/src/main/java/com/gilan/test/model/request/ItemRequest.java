package com.gilan.test.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest{
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotNull(message = "Price is mandatory")
    private Integer price;

}
