package com.codecool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierOrderDto {
    private JobsDto jobsDto;
    private int orderId;
    private boolean complete;

}
