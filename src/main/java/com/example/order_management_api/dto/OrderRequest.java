package com.example.order_management_api.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotBlank(message = "itemName is mandatory")
    private String itemName;

    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;

    @NotBlank(message = "shippingAddress is mandatory")
    private String shippingAddress;
}
