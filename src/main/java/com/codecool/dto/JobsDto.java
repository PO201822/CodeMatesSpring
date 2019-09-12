package com.codecool.dto;

import com.codecool.entity.CartItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsDto {
    private int cartId;
    private String username;
    private String location;
    private String address;
    private int quantity;
    private LocalDateTime date;
    private List<CartItems> cartItems;
}
