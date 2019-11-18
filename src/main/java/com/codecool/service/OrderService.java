package com.codecool.service;

import com.codecool.dto.CartOrderDto;
import com.codecool.dto.OrderDetailsDto;
import com.codecool.entity.Orders;

import java.util.List;

public interface OrderService {
    Orders findByCartId(int id);

    OrderDetailsDto updateOrderDetailsDtoByOrder(OrderDetailsDto orderDetails, Orders order);

    List<CartOrderDto> getOrders();

}
