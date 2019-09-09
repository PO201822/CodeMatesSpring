package com.codecool.repository;

import com.codecool.model.CartItems;
import com.codecool.model.Products;
import com.codecool.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {
    @Override
    <S extends CartItems> S save(S entity);

    void deleteByProductAndCart_User(Products p, Users user);
}
