package com.codecool.controller;

import com.codecool.entity.Orders;
import com.codecool.entity.Users;
import com.codecool.repository.OrdersRepository;
import com.codecool.repository.UserRepository;
import com.codecool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourierController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @GetMapping(path = "/courier/getMyCurrentJobs")
    public List<Orders> getMyCurrentJobs() {
        Users user = userRepository.findByName(userService.currentUser());
        return ordersRepository.findAllByCourierId(user.getId());
    }

}
