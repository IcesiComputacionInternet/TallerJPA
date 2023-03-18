package com.Icesi.TallerJPA.error;

import com.Icesi.TallerJPA.error.exception.IcesiError;
import com.Icesi.TallerJPA.error.exception.IcesiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<IcesiError> handleException(IcesiException icesiException) {
        return new ResponseEntity<>(icesiException.getError(), icesiException.getHttpStatus());
    }
}
