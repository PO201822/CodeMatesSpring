package com.codecool.repository;

import com.codecool.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {


    List<Orders> findAllByCourierId(int courierId);
}
