package com.example.jpa.error.exceptions;

import com.example.jpa.error.util.IcesiError;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private IcesiError icesiError;

    public ValidationException(IcesiError icesiError) {
        super(icesiError.toString());
        this.icesiError = icesiError;
    }

    public ValidationException(String message) {
        super(message);
    }

}
