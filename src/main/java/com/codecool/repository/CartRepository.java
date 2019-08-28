package com.codecool.repository;

import com.codecool.model.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Carts, Integer> {

    @Override
    <S extends Carts> S save(S entity);
}