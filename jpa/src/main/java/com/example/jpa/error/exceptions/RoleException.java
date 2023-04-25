package com.example.jpa.error.exceptions;

import com.example.jpa.error.util.IcesiError;

public class RoleException extends RuntimeException {

    private IcesiError icesiError;

    public RoleException(IcesiError icesiError) {
        super();
        this.icesiError = icesiError;
    }

    public RoleException(String message) {
        super(message);
    }

}
