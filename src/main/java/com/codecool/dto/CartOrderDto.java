package com.codecool.dto;

import com.codecool.entity.Carts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderDto {
    private Carts cart;
    private boolean isComplete;
    private LocalDateTime completionDate;

}
