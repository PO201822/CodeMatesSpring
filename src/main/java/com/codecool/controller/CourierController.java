package com.codecool.controller;

import com.codecool.dto.CartIdDto;
import com.codecool.dto.CourierOrderDto;
import com.codecool.dto.JobsDto;
import com.codecool.dto.OrderIdDto;
import com.codecool.entity.CartItems;
import com.codecool.entity.Carts;
import com.codecool.entity.Orders;
import com.codecool.entity.Users;
import com.codecool.repository.CartRepository;
import com.codecool.repository.OrdersRepository;
import com.codecool.repository.UserRepository;
import com.codecool.service.simple.SimpleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CourierController {


    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping(path = "/courier/getMyCurrentJobs")
    public List<CourierOrderDto> getMyCurrentJobs() {
        Users user = simpleUserService.getCurrentUser();
        List<Orders> allByCourierId = ordersRepository.findAllByCourierIdAndComplete(user.getId(), false);

        if (allByCourierId.size() == 0){
            return null;
        }

        List<CourierOrderDto> courierOrderDtoList = new ArrayList<>();
        for (Orders o : allByCourierId) {
            List<CartItems> cartItems = o.getCart().getCartItems();
            Carts carts = o.getCart();
            int quantity = 0;
            for (CartItems ci : cartItems) {
                quantity += ci.getQuantity();
            }

            JobsDto jobsDto = new JobsDto(carts.getId(), carts.getUser().getName(), carts.getUser().getLocation(), carts.getUser().getAddress(),
                    quantity, carts.getCheckout_date(), carts.getCartItems());
            courierOrderDtoList.add(new CourierOrderDto(jobsDto, o.getId(), o.isComplete()));

        }

        return courierOrderDtoList;
    }

    @GetMapping(path = "/courier/getAllJobs")
    public List<JobsDto> getAllJobs() {
        List<Carts> carts = cartRepository.findAllByCheckedOutAndPickedup(true, false);
        if (carts.size() == 0) {
            return null;
        }
        List<JobsDto> jobsDtos = new ArrayList<>();
        int quantity = 0;
        for (Carts c : carts) {
            List<CartItems> cartItems = c.getCartItems();
            for (CartItems ci : cartItems) {
                quantity += ci.getQuantity();
            }
            jobsDtos.add(new JobsDto(c.getId(), c.getUser().getName(), c.getUser().getLocation(), c.getUser().getAddress(), quantity, c.getCheckout_date(), c.getCartItems()));
        }
        return jobsDtos;
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
        orders.setComplition_date(LocalDateTime.now());
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
        Users user = simpleUserService.getCurrentUser();
        List<Orders> allByCourierId = ordersRepository.findAllByCourierIdAndComplete(user.getId(), true);

        if (allByCourierId.size() == 0){
            return null;
        }

        List<CourierOrderDto> courierOrderDtoList = new ArrayList<>();
        for (Orders o : allByCourierId) {
            List<CartItems> cartItems = o.getCart().getCartItems();
            Carts carts = o.getCart();
            int quantity = 0;
            for (CartItems ci : cartItems) {
                quantity += ci.getQuantity();
            }

            JobsDto jobsDto = new JobsDto(carts.getId(), carts.getUser().getName(), carts.getUser().getLocation(), carts.getUser().getAddress(),
                    quantity, carts.getCheckout_date(), carts.getCartItems());
            courierOrderDtoList.add(new CourierOrderDto(jobsDto, o.getId(), o.isComplete(),o.getComplition_date()));

        }

        return courierOrderDtoList;
    }
}
