package com.codecool.repository;

import com.codecool.entity.Restaurants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantsRepostitory extends JpaRepository<Restaurants, Integer> {

    List<Restaurants> findByLocationIgnoreCase(String location);
    Restaurants findById(int id);

    @Override
    <S extends Restaurants> S save(S entity);

}
