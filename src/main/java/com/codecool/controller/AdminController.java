package com.codecool.controller;

import com.codecool.dto.AdminRestaurantDto;
import com.codecool.dto.RestaurantDetailsDto;
import com.codecool.entity.Restaurants;
import com.codecool.exception.InvalidUserCredentialsException;
import com.codecool.exception.RestaurantAlreadyExistsException;
import com.codecool.repository.RestaurantsRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private RestaurantsRepostitory restaurantsRepostitory;

    @PostMapping(path = "/admin/addRestaurant")
    @ResponseBody
    void newRestaurant(@RequestBody AdminRestaurantDto restaurant) {
        if (restaurantsRepostitory.findByNameAndLocation(restaurant.getName(), restaurant.getLocation()) != null) {
            throw new RestaurantAlreadyExistsException("Restaurant already exists! Change restaurant's name and/or location!");
        }
        restaurantsRepostitory.save(new Restaurants(restaurant.getName(), restaurant.getLocation(), restaurant.getPicture(), restaurant.getDescription()));
    }

    @GetMapping(path = "/admin/listAllRestaurants")
    @ResponseBody
    List<Restaurants> listAllRestaurants() {
        return restaurantsRepostitory.findAll();
    }

    @DeleteMapping(path = "/admin/removeRestaurant")
    public @ResponseBody
    void removeRestaurant(@RequestParam int id) {
        Restaurants restaurant = restaurantsRepostitory.findById(id);
        restaurantsRepostitory.delete(restaurant);
    }

    @GetMapping(path = "/admin/restaurant/{id}")
    @ResponseBody
    Restaurants getRestaurant(@PathVariable(value = "id") int id) {
        return restaurantsRepostitory.findById(id);
    }


    @PutMapping(path = "/admin/updateRestaurant")
    public @ResponseBody
    void updateRestaurant(@RequestBody RestaurantDetailsDto updatedRestaurant) {
        Restaurants restaurant = restaurantsRepostitory.findById(updatedRestaurant.getId());

        if (!restaurant.getName().equals(updatedRestaurant.getName())
                || !restaurant.getLocation().equals(updatedRestaurant.getLocation())) {
            if (restaurantsRepostitory.findByNameAndLocation(updatedRestaurant.getName(), updatedRestaurant.getLocation()) != null) {
                throw new InvalidUserCredentialsException("ALREADY EXISTS");
            }
        }

        restaurant.setName(updatedRestaurant.getName());
        restaurant.setDescription(updatedRestaurant.getDescription());
        restaurant.setLocation(updatedRestaurant.getLocation());
        restaurant.setPicture(updatedRestaurant.getPicture());

        restaurantsRepostitory.save(restaurant);

    }


}
