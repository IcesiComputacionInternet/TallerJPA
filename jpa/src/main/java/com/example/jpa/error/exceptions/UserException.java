package com.example.jpa.error.exceptions;

import com.example.jpa.error.util.IcesiError;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private IcesiError icesiError;

    public UserException(IcesiError icesiError) {
        super();
        this.icesiError = icesiError;
    }

    public UserException(String message) {
        super(message);
    }

}
