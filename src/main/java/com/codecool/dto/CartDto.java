package com.codecool.dto;

import com.codecool.entity.Carts;
import com.codecool.entity.Products;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Products products;
    private int quantity;
    private Carts cart;


}
