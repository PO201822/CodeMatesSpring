package com.codecool.dto;

import com.codecool.entity.CartItems;
import com.codecool.entity.Carts;
import com.codecool.entity.Users;
import com.codecool.model.CartItemDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderDto {

    private Carts cart;
    private List<CartItems> cartItems;
    private CartItemDetail cartItemDetail;
    private Users costumer;
    private String status;
}
