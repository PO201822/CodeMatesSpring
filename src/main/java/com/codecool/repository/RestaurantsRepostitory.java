package com.codecool.repository;

import com.codecool.model.Restaurants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantsRepostitory extends JpaRepository<Restaurants, Integer> {

    List<Restaurants> findByLocationIgnoreCase(String location);
}
