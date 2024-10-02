package com.example.order_management_api.repository;

import com.example.order_management_api.model.Order;
import com.example.order_management_api.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByStatus(OrderStatus status);
}
