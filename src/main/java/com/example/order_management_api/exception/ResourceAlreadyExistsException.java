package com.example.order_management_api.exception;

// Exception when a Resource Already exist
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}

