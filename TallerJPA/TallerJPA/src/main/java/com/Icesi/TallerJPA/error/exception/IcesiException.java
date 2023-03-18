package com.Icesi.TallerJPA.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class IcesiException extends RuntimeException {

    private HttpStatus httpStatus;
    private IcesiError error;
}
