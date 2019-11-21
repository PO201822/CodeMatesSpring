package com.codecool.service.simple;

import com.codecool.dto.CourierOrderDto;
import com.codecool.dto.JobsDto;
import com.codecool.entity.CartItems;
import com.codecool.entity.Carts;
import com.codecool.entity.Orders;
import com.codecool.entity.Users;
import com.codecool.repository.CartRepository;
import com.codecool.repository.OrdersRepository;
import com.codecool.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleCourierService implements CourierService {

    @Autowired
    private SimpleUserService simpleUserService;


    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<CourierOrderDto> getMyCurrentJobs() {
        Users user = simpleUserService.getCurrentUser();
        List<Orders> allByCourierId = ordersRepository.findAllByCourierIdAndComplete(user.getId(), false);

        if (allByCourierId.size() == 0) {
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

    @Override
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

    @Override
    public List<CourierOrderDto> getCompletedJobs() {
        Users user = simpleUserService.getCurrentUser();
        List<Orders> allByCourierId = ordersRepository.findAllByCourierIdAndComplete(user.getId(), true);

        if (allByCourierId.size() == 0) {
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
            courierOrderDtoList.add(new CourierOrderDto(jobsDto, o.getId(), o.isComplete(), o.getCompletion_date()));

        }

        return courierOrderDtoList;
    }
}
