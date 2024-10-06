package com.example.order_management_api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.order_management_api.dto.OrderDto;
import com.example.order_management_api.dto.OrderRequest;
import com.example.order_management_api.model.Client;
import com.example.order_management_api.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;


public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        client = new Client().setEmail("client@example.com");
    }

    // Test case for successfully placing an order
    @Test
    void testPlaceOrderSuccess() throws Exception {
        OrderRequest orderRequest = new OrderRequest("Laptop", 1, "123 Main St");
        when(orderService.placeOrder(any(OrderRequest.class), any(Client.class))).thenReturn("REF123");

        mockMvc.perform(post("/api/orders/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest))
                        .principal(() -> "client@example.com"))  // Mock client authentication
                .andExpect(status().isOk())
                .andExpect(content().string("Order placed successfully. Reference Number: REF123"));
    }

    // Test case for successfully canceling an order
    @Test
    void testCancelOrderSuccess() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("client@example.com");

        mockMvc.perform(patch("/api/orders/cancel/REF123")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(content().string("Order cancelled successfully"));

        verify(orderService, times(1)).cancelOrder("REF123", "client@example.com");
    }

    // Test case for fetching the order history of the client
    @Test
    void testFetchOrderHistory() throws Exception {
        // Mock the client and order history
        Pageable pageable = PageRequest.of(0, 5);
        Page<OrderDto> orderHistory = new PageImpl<>(Collections.singletonList(new OrderDto(
                "REF123", "Laptop", 1, "123 Main St", LocalDateTime.now(), "NEW")), pageable, 1);

        when(orderService.getOrderHistory(any(Client.class), eq(0), eq(5))).thenReturn(orderHistory);

        mockMvc.perform(get("/api/orders/history")
                        .param("pageNo", "0")
                        .param("pageSize", "5")
                        .principal(() -> "client@example.com"))  // Mock client authentication
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].referenceNumber").value("REF123"))
                .andExpect(jsonPath("$.content[0].itemName").value("Laptop"));

        verify(orderService, times(1)).getOrderHistory(any(Client.class), eq(0), eq(5));
    }

}
