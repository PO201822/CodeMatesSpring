package com.codecool.controller;

import com.codecool.model.Restaurants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
public class RestaurantsController {
    @PersistenceContext
    public EntityManager em;

    @GetMapping(path="/restaurants")
    public @ResponseBody
    Iterable<Restaurants> getRestaurantWhereProductNameIs() {
        Iterable<Restaurants> r = em.createNamedQuery("findRatedRestaurants").setParameter("iduser", 2).getResultList();
        return r;
    }

}
