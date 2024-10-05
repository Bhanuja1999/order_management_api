package com.example.order_management_api.controller;

import com.example.order_management_api.dto.OrderRequest;
import com.example.order_management_api.model.Client;
import com.example.order_management_api.model.Order;
import com.example.order_management_api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/orders") // Base URL for order-related endpoints
public class OrderController {

    @Autowired
    private OrderService orderService;

    //Places a new order
    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest, @AuthenticationPrincipal Client client) {
        String orderRef = orderService.placeOrder(orderRequest, client);
        return ResponseEntity.ok("Order placed successfully. Reference Number: " + orderRef);
    }

    //Cancels an existing order
    @PatchMapping("/cancel/{referenceNumber}")
    public ResponseEntity<String> cancelOrder(@PathVariable String referenceNumber, Principal principal) {
        String clientEmail = principal.getName();
        orderService.cancelOrder(referenceNumber, clientEmail);
        return ResponseEntity.ok("Order cancelled successfully");
    }

    //Fetches the order history for the authenticated client
    @GetMapping("/history")
    public ResponseEntity<Page<Order>> fetchOrderHistory(
            @RequestParam int pageNo,
            @RequestParam int pageSize,
            @AuthenticationPrincipal Client client) {

        Page<Order> orderHistory = orderService.getOrderHistory(client, pageNo, pageSize);
        return ResponseEntity.ok(orderHistory);
    }



}

