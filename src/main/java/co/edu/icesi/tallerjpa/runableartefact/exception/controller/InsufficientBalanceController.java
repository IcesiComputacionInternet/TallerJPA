package co.edu.icesi.tallerjpa.runableartefact.exception.controller;

import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.InsufficientBalance;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InsufficientBalanceController {
    @ExceptionHandler(value = InsufficientBalance.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public String handleUserExistsException(InsufficientBalance e){
        e.printStackTrace();
        return e.getMessage();
    }
}