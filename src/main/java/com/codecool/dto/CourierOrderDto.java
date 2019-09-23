package com.codecool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierOrderDto {
    private JobsDto jobsDto;
    private int orderId;
    private boolean complete;
    private LocalDateTime completionDate;

    public CourierOrderDto(JobsDto jobsDto, int orderId, boolean complete) {
        this.jobsDto = jobsDto;
        this.orderId = orderId;
        this.complete = complete;
    }
}
