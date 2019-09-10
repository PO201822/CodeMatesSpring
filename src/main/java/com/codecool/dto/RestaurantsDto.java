package com.codecool.dto;

import com.codecool.entity.Products;
import com.codecool.entity.Restaurants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantsDto {
    private List<Products> products;
    private Restaurants restaurant;
}
