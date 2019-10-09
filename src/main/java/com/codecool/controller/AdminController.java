package com.codecool.controller;

import com.codecool.dto.AdminRestaurantDto;
import com.codecool.entity.Restaurants;
import com.codecool.repository.RestaurantsRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    @Autowired
    private RestaurantsRepostitory rr;

    @PostMapping(path = "/admin/addRestaurant")
    @ResponseBody
    void newRestaurant(@RequestBody AdminRestaurantDto restaurant) {
        rr.save(new Restaurants(restaurant.getName(), restaurant.getLocation(), restaurant.getPicture(), restaurant.getDescription()));
    }
}
