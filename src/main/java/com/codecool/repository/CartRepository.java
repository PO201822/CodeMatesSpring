package com.codecool.repository;

import com.codecool.entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Carts, Integer> {

    @Override
    <S extends Carts> S save(S entity);

    List<Carts> findAllByUserId(int userId);

    List<Carts> findAllByCheckedOutAndPickedup(boolean checkedOut, boolean pickedUp);

    List<Carts> findAllByUserIdAndCheckedOut(int userId, boolean checkedOut);

    Optional<Carts> findByUserIdAndCheckedOut(int userId, boolean checkedOut);

    Carts findById(int id);


}
