package com.codecool.dto;

import com.codecool.model.CartItemDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDto {

    private boolean isCompleted;
    private LocalDateTime completionDate;
    private CartItemDetail cartItemDetail;
    private String status;

}
