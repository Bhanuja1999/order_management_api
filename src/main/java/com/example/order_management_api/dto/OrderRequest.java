package com.example.order_management_api.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String itemName;
    private int quantity;
    private String shippingAddress;
}
