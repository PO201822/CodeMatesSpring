package com.codecool.repository;

import com.codecool.entity.CartItems;
import com.codecool.entity.Products;
import com.codecool.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {
    @Override
    <S extends CartItems> S save(S entity);

    CartItems findById(int id);

    void deleteByProductAndCart_User(Products p, Users user);
}
