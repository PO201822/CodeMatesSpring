package com.codecool.controller;

import com.codecool.model.Products;
import com.codecool.model.Restaurants;
import com.codecool.model.RestaurantsDto;
import com.codecool.repository.RestaurantsRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@RestController
public class RestaurantsController {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RestaurantsRepostitory restaurantsRepostitory;

    @GetMapping(path = "/restaurants")
    public @ResponseBody
    Iterable<Restaurants> getRestaurantsWhereLocationIs(@RequestParam(value = "location") String location) {
        String trimmedLoc = location.replaceAll("\"", "");
        return restaurantsRepostitory.findByLocationIgnoreCase(trimmedLoc);
    }

    @GetMapping(path = "/restaurant/{id}")
    public @ResponseBody
    RestaurantsDto getRestaurantsWhereLocationIs(@PathVariable(value = "id") int id) {
        TypedQuery<Products> query =
                em.createNamedQuery("findAllByRestaurantId", Products.class).setParameter("restaurantid", id);
        List<Products> products = query.getResultList();
        Restaurants restaurant = restaurantsRepostitory.findById(id);
        return new RestaurantsDto(products, restaurant);
    }
}
