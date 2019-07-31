package com.codecool.repository;

import com.codecool.model.Users;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<Users, Integer> {

}
