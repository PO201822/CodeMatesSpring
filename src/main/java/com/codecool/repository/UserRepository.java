package com.codecool.repository;

import com.codecool.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Users, Integer> {

    Users findByName(String name);

    Users findByEmail(String email);

    @Override
    <S extends Users> S save(S entity);
}
