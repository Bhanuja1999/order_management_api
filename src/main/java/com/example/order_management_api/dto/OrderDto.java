package com.example.order_management_api.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String referenceNumber;
    private String itemName;
    private int quantity;
    private String shippingAddress;
    private LocalDateTime placementTimestamp;
    private String status;

}
