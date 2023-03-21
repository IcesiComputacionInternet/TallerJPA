package com.example.jpa.dto;

public class InactiveAccountException extends RuntimeException {
    public InactiveAccountException(String message) {
        super(message);
    }
}
