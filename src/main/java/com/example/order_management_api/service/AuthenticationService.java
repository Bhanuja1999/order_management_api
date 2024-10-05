package com.example.order_management_api.service;

import com.example.order_management_api.dto.LoginClientDto;
import com.example.order_management_api.dto.RegisterClientDto;
import com.example.order_management_api.model.Client;
import com.example.order_management_api.repository.ClientRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            ClientRepository clientRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Client signup(RegisterClientDto input) {
        Client client = new Client()
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setFirstName(input.getFirstName())
                .setLastName(input.getLastName());

        return clientRepository.save(client);
    }

    public Client authenticate(LoginClientDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return clientRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
