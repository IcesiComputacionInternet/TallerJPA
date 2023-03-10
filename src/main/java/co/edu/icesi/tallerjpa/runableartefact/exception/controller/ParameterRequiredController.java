package co.edu.icesi.tallerjpa.runableartefact.exception.controller;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.ParameterRequired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ParameterRequiredController {

    @ExceptionHandler(value = ParameterRequired.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public String handleUserExistsException(ParameterRequired e){
        e.printStackTrace();
        return e.getMessage();
    }
}
