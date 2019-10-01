package com.codecool.service.simple;

import com.codecool.dto.OrderDetailsDto;
import com.codecool.entity.Orders;
import com.codecool.repository.OrdersRepository;
import com.codecool.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleOrderService implements OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    public Orders findByCartId(int id) {
        return ordersRepository.findByCartId(id);

    }

    public OrderDetailsDto updateOrderDetailsDtoByOrder(OrderDetailsDto orderDetails, Orders order){
        orderDetails.setCompleted(order.isComplete());
        String status = order.isComplete() ? "Completed" : "Delivering";
        orderDetails.setStatus(status);
        orderDetails.setCompletionDate(order.getComplition_date());
        return orderDetails;
    }
}
