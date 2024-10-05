package com.example.order_management_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginClientDto {

    @NotBlank(message = "email is mandatory")
    private String email;

    @NotBlank(message = "password is mandatory")
    private String password;

}
