package com.example.order_management_api.exception;

// Exception when requested resource is not found
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
