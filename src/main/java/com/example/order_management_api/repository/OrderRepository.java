package com.example.order_management_api.repository;

import com.example.order_management_api.model.Client;
import com.example.order_management_api.model.Order;
import com.example.order_management_api.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByStatus(OrderStatus status);
    Optional<Order> findByReferenceNumberAndClient_Email(String referenceNumber, String email);
    Page<Order> findByClient(Client client, Pageable pageable);

}
