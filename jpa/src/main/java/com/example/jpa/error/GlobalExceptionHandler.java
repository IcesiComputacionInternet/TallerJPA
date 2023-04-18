package com.example.jpa.error;

import com.example.jpa.exceptions.AccountNotFoundException;
import com.example.jpa.exceptions.AccountTypeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException runtimeException){
        System.out.println("Error: " + runtimeException.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public void handleAccountNotFoundException(AccountNotFoundException accountNotFoundException){
        System.out.println("Error: " + accountNotFoundException.getMessage());
    }

    @ExceptionHandler(AccountTypeException.class)
    public void handleAccountTypeException(AccountTypeException accountTypeException){
        System.out.println("Error: " + accountTypeException.getMessage());
    }

    //TODO Add one exception handler for each exception you have created
}
