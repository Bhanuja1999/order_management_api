package com.example.order_management_api.service;

import com.example.order_management_api.model.Order;
import com.example.order_management_api.model.OrderStatus;
import com.example.order_management_api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

//Scheduled method to dispatch new orders
@Service
public class OrderDispatchJob {

    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(cron = "0 0 * * * ?")
    public void dispatchNewOrders() {
        List<Order> newOrders = orderRepository.findByStatus(OrderStatus.NEW);
        for (Order order : newOrders) {
            order.setStatus(OrderStatus.DISPATCHED);
            orderRepository.save(order);
        }
    }
}

