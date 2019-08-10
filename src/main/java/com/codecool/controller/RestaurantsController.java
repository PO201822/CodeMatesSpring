package com.codecool.controller;

import com.codecool.model.Restaurants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Controller
public class RestaurantsController {
    @PersistenceContext
    public EntityManager em;

    @GetMapping(path="/restaurants")
    public @ResponseBody
    Iterable<Restaurants> getRestaurantWhereProductNameIs() {
        Iterable<Restaurants> r = em.createNamedQuery("findsomething").setParameter("prodname", "CheeseBurger").getResultList();
        return r;
    }

}
