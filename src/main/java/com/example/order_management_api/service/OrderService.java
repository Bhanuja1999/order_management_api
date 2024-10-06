package com.example.order_management_api.service;

import com.example.order_management_api.dto.OrderDto;
import com.example.order_management_api.dto.OrderRequest;
import com.example.order_management_api.exception.ResourceNotFoundException;
import com.example.order_management_api.model.Client;
import com.example.order_management_api.model.Order;
import com.example.order_management_api.model.OrderStatus;
import com.example.order_management_api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    //Places a new order for a client
    public String placeOrder(OrderRequest orderRequest, Client client) {
        Order order = new Order();
        order.setItemName(orderRequest.getItemName())
                .setQuantity(orderRequest.getQuantity())
                .setShippingAddress(orderRequest.getShippingAddress())
                .setPlacementTimestamp(LocalDateTime.now())
                .setStatus(OrderStatus.NEW)
                .setClient(client);

        order = orderRepository.save(order);
        return order.getReferenceNumber();
    }

    //Cancels an existing order if its status is NEW
    public void cancelOrder(String referenceNumber, String clientEmail) {
        Order order = orderRepository.findByReferenceNumberAndClient_Email(referenceNumber, clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Check if the order status is NEW
        if (order.getStatus() != OrderStatus.NEW) {
            throw new IllegalStateException("Cannot cancel order in status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    //Retrieves the order history for a specific client with pagination
    public Page<OrderDto> getOrderHistory(Client client, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Order> orderPage = orderRepository.findByClient(client, pageable);

        return orderPage.map(order -> new OrderDto(
                order.getReferenceNumber(),
                order.getItemName(),
                order.getQuantity(),
                order.getShippingAddress(),
                order.getPlacementTimestamp(),
                order.getStatus().toString()
        ));
    }
}

