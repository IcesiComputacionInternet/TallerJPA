package com.example.jpa.exceptions;

public class NotFoundRoleException extends RuntimeException{

    public NotFoundRoleException(String message) {
        super(message);
    }
}
