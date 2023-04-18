package com.example.TallerJPA.error;

import com.example.TallerJPA.error.exception.IcesiError;
import com.example.TallerJPA.error.exception.IcesiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IcesiException.class)
    public ResponseEntity<IcesiError> handleIcesiException(IcesiException exception){
        return ResponseEntity.status(exception.getError().getStatus()).body(exception.getError());
    }
}
