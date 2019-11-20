package com.codecool.service.simple;

import com.codecool.dto.CartOrderDto;
import com.codecool.dto.OrderDetailsDto;
import com.codecool.entity.Carts;
import com.codecool.entity.Orders;
import com.codecool.model.CartItemDetail;
import com.codecool.repository.OrdersRepository;
import com.codecool.service.CartItemsService;
import com.codecool.service.CartService;
import com.codecool.service.OrderService;
import com.codecool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleOrderService implements OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserService simpleUserService;

    @Autowired
    private CartService simpleCartService;

    @Autowired
    private CartItemsService simpleCartItemsService;

    @Autowired
    private OrderService simpleOrderService;

    public Orders findByCartId(int id) {
        return ordersRepository.findByCartId(id);

    }

    public OrderDetailsDto updateOrderDetailsDtoByOrder(OrderDetailsDto orderDetails, Orders order) {
        orderDetails.setCompleted(order.isComplete());
        String status = order.isComplete() ? "Completed" : "Delivering";
        orderDetails.setStatus(status);
        orderDetails.setCompletionDate(order.getComplition_date());
        return orderDetails;
    }

    @Override
    public List<CartOrderDto> getOrders() {
        List<CartOrderDto> cartOrderDtos = new ArrayList<>();
        List<Carts> carts = simpleCartService.getAvailableCarts(simpleUserService.getCurrentUser());
        if (carts.size() == 0) {
            return null;
        }

        for (Carts cart : carts) {
            OrderDetailsDto orderDetails = new OrderDetailsDto(false, null, new CartItemDetail(0, 0), "Waiting for pick up");
            CartItemDetail cartItemDetail = simpleCartItemsService.calculateCartItemsDetail(cart);
            if (cart.isPickedup()) {
                Orders order = simpleOrderService.findByCartId(cart.getId());
                simpleOrderService.updateOrderDetailsDtoByOrder(orderDetails, order);
            }
            cartOrderDtos.add(new CartOrderDto(cart, cart.getCartItems(), cartItemDetail, cart.getUser(), orderDetails.getStatus()));
        }
        return cartOrderDtos;
    }
}
