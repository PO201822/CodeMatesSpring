package com.codecool.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class CartItemDetail {

    private int totalPrice;
    private int quantity;

}
