package com.codecool.repository;

import com.codecool.entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Carts, Integer> {

    @Override
    <S extends Carts> S save(S entity);

    List<Carts> findAllByUserId(int userId);

    List<Carts> findAllByCheckedOutAndPickedup(boolean checkedOut, boolean pickedUp);

    Carts findById(int id);
}
