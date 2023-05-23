package com.icesi.TallerJPA.error.exception;

import lombok.Getter;

@Getter
public class IcesiException extends RuntimeException {

    private final IcesiError error;
    public IcesiException(String msg, IcesiError error) {
        super(msg);
        this.error = error;
    }
}


