package com.example.jpa.error.exceptions;

import com.example.jpa.error.util.IcesiError;

public class AccountException extends RuntimeException {

    private IcesiError icesiError;

    public AccountException(IcesiError icesiError) {
        super();
        this.icesiError = icesiError;
    }

    public AccountException(String message) {
        super(message);
    }
}
