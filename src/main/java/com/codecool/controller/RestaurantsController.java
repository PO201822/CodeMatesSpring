package com.codecool.controller;

import com.codecool.model.Restaurants;
import com.codecool.repository.RestaurantsRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
public class RestaurantsController {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RestaurantsRepostitory restaurantsRepostitory;

    @GetMapping(path="/restaurants")
    public @ResponseBody
    Iterable<Restaurants> getRestaurantsWhereLocationIs(@RequestParam(value = "location") String location) {
        String trimmedLoc = location.replaceAll("\"", "");
        return restaurantsRepostitory.findByLocationIgnoreCase(trimmedLoc);
    }
}
