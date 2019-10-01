package com.codecool.service;

import com.codecool.dto.OrderDetailsDto;
import com.codecool.entity.Orders;

public interface OrderService {
    Orders findByCartId(int id);

    OrderDetailsDto updateOrderDetailsDtoByOrder(OrderDetailsDto orderDetails, Orders order);

}
