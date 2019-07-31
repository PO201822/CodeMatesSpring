package com.codecool.repository;

import com.codecool.model.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<Users, Integer> {

    Optional<Users> findByName(String name);

}
