package com.example.order_management_api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.order_management_api.dto.OrderDto;
import com.example.order_management_api.dto.OrderRequest;
import com.example.order_management_api.exception.ResourceNotFoundException;
import com.example.order_management_api.model.Client;
import com.example.order_management_api.model.Order;
import com.example.order_management_api.model.OrderStatus;
import com.example.order_management_api.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Client client;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new Client().setEmail("client@example.com");
        order = new Order().setReferenceNumber("REF123")
                .setItemName("Laptop")
                .setQuantity(1)
                .setShippingAddress("123 Main St")
                .setPlacementTimestamp(LocalDateTime.now())
                .setStatus(OrderStatus.NEW)
                .setClient(client);
    }

    // Test for placing an order successfully
    @Test
    void testPlaceOrder() {
        OrderRequest orderRequest = new OrderRequest("Laptop", 1, "123 Main St");
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        String referenceNumber = orderService.placeOrder(orderRequest, client);

        assertEquals("REF123", referenceNumber);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    // Test for successfully canceling an order
    @Test
    void testCancelOrder_Success() {
        when(orderRepository.findByReferenceNumberAndClient_Email("REF123", "client@example.com"))
                .thenReturn(Optional.of(order));

        orderService.cancelOrder("REF123", "client@example.com");

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    // Test for handling a case where the order to be canceled is not found
    @Test
    void testCancelOrder_OrderNotFound() {
        when(orderRepository.findByReferenceNumberAndClient_Email("REF123", "client@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.cancelOrder("REF123", "client@example.com"));
    }

    // Test for handling a case where an order cannot be canceled because it is not in the NEW status
    @Test
    void testCancelOrder_OrderNotNew() {
        order.setStatus(OrderStatus.DISPATCHED);
        when(orderRepository.findByReferenceNumberAndClient_Email("REF123", "client@example.com"))
                .thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> orderService.cancelOrder("REF123", "client@example.com"));
    }

    // Test for retrieving order history with pagination
    @Test
    void testGetOrderHistory() {
        // Mock pageable and page result
        Pageable pageable = PageRequest.of(0, 5);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order), pageable, 1);

        when(orderRepository.findByClient(client, pageable)).thenReturn(orderPage);

        Page<OrderDto> result = orderService.getOrderHistory(client, 0, 5);

        // Verify the order history mapping
        assertEquals(1, result.getTotalElements());
        OrderDto dto = result.getContent().get(0);
        assertEquals(order.getReferenceNumber(), dto.getReferenceNumber());
        assertEquals(order.getItemName(), dto.getItemName());
        assertEquals(order.getQuantity(), dto.getQuantity());
        assertEquals(order.getShippingAddress(), dto.getShippingAddress());

        verify(orderRepository, times(1)).findByClient(client, pageable);
    }

}
