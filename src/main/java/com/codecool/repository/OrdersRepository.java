package com.codecool.repository;

import com.codecool.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    @Override
    <S extends Orders> S save(S entity);

    List<Orders> findAllByCourierId(int courierId);
}
