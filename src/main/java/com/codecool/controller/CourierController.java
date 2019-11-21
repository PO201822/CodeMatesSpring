package com.codecool.controller;

import com.codecool.dto.CartIdDto;
import com.codecool.dto.CourierOrderDto;
import com.codecool.dto.JobsDto;
import com.codecool.dto.OrderIdDto;
import com.codecool.entity.Carts;
import com.codecool.entity.Orders;
import com.codecool.entity.Users;
import com.codecool.repository.CartRepository;
import com.codecool.repository.OrdersRepository;
import com.codecool.service.simple.SimpleCourierService;
import com.codecool.service.simple.SimpleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CourierController {


    @Autowired
    private SimpleUserService simpleUserService;


    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private SimpleCourierService simpleCourierService;

    @GetMapping(path = "/courier/getMyCurrentJobs")
    public List<CourierOrderDto> getMyCurrentJobs() {
        return simpleCourierService.getMyCurrentJobs();
    }

    @GetMapping(path = "/courier/getAllJobs")
    public List<JobsDto> getAllJobs() {
        return simpleCourierService.getAllJobs();
    }

    @PostMapping(path = "/courier/pickUpJob")
    @ResponseBody
    void pickUpJob(@RequestBody CartIdDto cartId) {
        Users courier = simpleUserService.getCurrentUser();
        Carts cart = cartRepository.findById(cartId.getCartId());
        cart.setPickedup(true);
        cartRepository.save(cart);
        ordersRepository.save(new Orders(courier, cart.getUser(), false, cart));

    }

    @PutMapping(path = "/courier/completeOrder")
    public @ResponseBody
    void completeOrder(@RequestBody OrderIdDto orderId) {
        Orders orders = ordersRepository.findById(orderId.getOrderId());
        orders.setComplete(true);
        orders.setCompletion_date(LocalDateTime.now());
        ordersRepository.save(orders);
    }

    @PutMapping(path = "/courier/dismissOrder")
    public @ResponseBody
    void dismissOrder(@RequestBody OrderIdDto orderId) {
        Orders orders = ordersRepository.findById(orderId.getOrderId());
        orders.getCart().setPickedup(false);
        Carts cart = cartRepository.findById(orders.getCart().getId());
        cart.setPickedup(false);
        cartRepository.save(cart);
        ordersRepository.delete(orders);
    }

    @GetMapping(path = "/courier/getCompleted")
    public List<CourierOrderDto> getCompleted() {
        return simpleCourierService.getMyCurrentJobs();
    }
}
