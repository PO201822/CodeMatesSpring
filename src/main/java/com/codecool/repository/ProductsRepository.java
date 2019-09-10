package com.codecool.repository;

import com.codecool.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {

    Products findById(int productId);
}
