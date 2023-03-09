package co.com.icesi.tallerjpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExistsController {

    @ExceptionHandler(value = ExistsException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public String handleUserExistsException(ExistsException e){
        e.printStackTrace();
        return e.getMessage();
    }
}
