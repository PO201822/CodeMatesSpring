package com.codecool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetailsDto {
    private int id;
    private String name;
    private String location;
    private String description;
    private String picture;
}
