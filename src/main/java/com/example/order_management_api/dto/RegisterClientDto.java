package com.example.order_management_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterClientDto {
    @NotBlank(message = "email is mandatory")
    private String email;

    @NotBlank(message = "password is mandatory")
    private String password;

    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    @NotBlank(message = "lastName is mandatory")
    private String lastName;
}
