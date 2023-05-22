package co.edu.icesi.tallerJPA.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExistingController {
    @ExceptionHandler(value = ExistingException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public String handleUserExistingException(ExistingException e){
        e.printStackTrace();
        return e.getMessage();
    }
}
