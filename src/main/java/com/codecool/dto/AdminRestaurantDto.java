package com.codecool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRestaurantDto {
    private String name;
    private String location;
    private String address;
    private String picture;
    private String description;
}
