package com.example.order_management_api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.example.order_management_api.dto.LoginClientDto;
import com.example.order_management_api.dto.RegisterClientDto;
import com.example.order_management_api.exception.ResourceAlreadyExistsException;
import com.example.order_management_api.model.Client;
import com.example.order_management_api.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Test case for a successful signup
    @Test
    void testSignupSuccess() {
        RegisterClientDto registerClientDto = new RegisterClientDto("client@example.com", "password", "Client", "User");

        when(clientRepository.findByEmail(registerClientDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerClientDto.getPassword())).thenReturn("encodedPassword");

        Client savedClient = new Client()
                .setEmail(registerClientDto.getEmail())
                .setPassword("encodedPassword")
                .setFirstName(registerClientDto.getFirstName())
                .setLastName(registerClientDto.getLastName());

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        Client result = authenticationService.signup(registerClientDto);

        assertEquals(savedClient.getEmail(), result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("Client", result.getFirstName());
        assertEquals("User", result.getLastName());
    }

    //Test case for attempting to sign up when a client already exists
    @Test
    void testSignupClientAlreadyExists() {
        RegisterClientDto registerClientDto = new RegisterClientDto("client@example.com", "password", "Client", "User");

        when(clientRepository.findByEmail(registerClientDto.getEmail())).thenReturn(Optional.of(new Client()));

        assertThrows(ResourceAlreadyExistsException.class, () -> authenticationService.signup(registerClientDto));
    }

    //Test case for a successful authentication
    @Test
    void testAuthenticateSuccess() {
        LoginClientDto loginClientDto = new LoginClientDto("client@example.com", "password");

        when(clientRepository.findByEmail(loginClientDto.getEmail())).thenReturn(Optional.of(new Client()));

        authenticationService.authenticate(loginClientDto);

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(
                loginClientDto.getEmail(), loginClientDto.getPassword()
        ));
    }

    //Test case for authentication failure when the client is not found
    @Test
    void testAuthenticateClientNotFound() {
        LoginClientDto loginClientDto = new LoginClientDto("client@example.com", "password");

        when(clientRepository.findByEmail(loginClientDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(loginClientDto));
    }
}

