package com.example.jpa.error.exceptions;

import com.example.jpa.error.util.IcesiError;
import lombok.Getter;

@Getter
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
