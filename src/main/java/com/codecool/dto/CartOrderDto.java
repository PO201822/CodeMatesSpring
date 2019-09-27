package com.codecool.dto;

import com.codecool.entity.CartItems;
import com.codecool.entity.Carts;
import com.codecool.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderDto {
    private Carts cart;
    private boolean isComplete;
    private LocalDateTime completionDate;
    private List<CartItems> cartItems;
    private int quantity;
    private int total;
    private String status;
    private Users costumer;

}
