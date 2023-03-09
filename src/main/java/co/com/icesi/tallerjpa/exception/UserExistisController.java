package co.com.icesi.tallerjpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExistisController {

    @ExceptionHandler(value = UserExistsException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public String handleUserExistsException(UserExistsException e){
        e.printStackTrace();
        return e.getMessage();
    }
}
