package com.example.order_management_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterClientDto {
    private String email;

    private String password;

    private String firstName;

    private String lastName;
}
