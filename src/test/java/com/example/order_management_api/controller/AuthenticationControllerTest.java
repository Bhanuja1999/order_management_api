package com.example.order_management_api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.order_management_api.dto.LoginClientDto;
import com.example.order_management_api.dto.RegisterClientDto;
import com.example.order_management_api.model.Client;
import com.example.order_management_api.service.AuthenticationService;
import com.example.order_management_api.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    // Test case for a successful signup
    @Test
    void testSignupSuccess() throws Exception {
        RegisterClientDto registerClientDto = new RegisterClientDto("client@example.com", "password", "Client", "User");
        Client savedClient = new Client().setEmail("client@example.com").setFirstName("Client").setLastName("User");

        when(authenticationService.signup(any(RegisterClientDto.class))).thenReturn(savedClient);

        // Performs a POST request to the /auth/signup endpoint
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerClientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("client@example.com"))
                .andExpect(jsonPath("$.firstName").value("Client"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    // Test case for a successful login
    @Test
    void testLoginSuccess() throws Exception {
        LoginClientDto loginClientDto = new LoginClientDto("client@example.com", "password");
        Client authenticatedClient = new Client().setEmail("client@example.com").setFirstName("Client").setLastName("User");

        when(authenticationService.authenticate(any(LoginClientDto.class))).thenReturn(authenticatedClient);
        when(jwtService.generateToken(any(Client.class))).thenReturn("jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        // Performs a POST request to the /auth/login endpoint
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginClientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.expiresIn").value(3600L));
    }
}
