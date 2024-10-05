package com.example.order_management_api.controller;

import com.example.order_management_api.dto.LoginClientDto;
import com.example.order_management_api.dto.LoginResponse;
import com.example.order_management_api.dto.RegisterClientDto;
import com.example.order_management_api.model.Client;
import com.example.order_management_api.service.AuthenticationService;
import com.example.order_management_api.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Client> register(@RequestBody RegisterClientDto registerClientDto) {
        Client registeredUser = authenticationService.signup(registerClientDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginClientDto loginClientDto) {
        Client authenticatedUser = authenticationService.authenticate(loginClientDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}